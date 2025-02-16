package com.example.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

import com.google.auto.service.AutoService;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_21)
@SupportedAnnotationTypes("com.example.filter.GenerateFilters")
public class GenerateFiltersProcessor extends AbstractProcessor {

	final Queue<Element> elementsToProcess;


	public GenerateFiltersProcessor() {
		elementsToProcess = new ArrayDeque<>();
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		final var annotatedElements = roundEnv.getElementsAnnotatedWith(GenerateFilters.class);
		elementsToProcess.addAll(annotatedElements);

		final var elementsSize = elementsToProcess.size();
		for (int i = 0; i < elementsSize; ++i) {
			// Remove first element from the queue.
			final var element = elementsToProcess.poll();

			processingEnv
					.getMessager()
					.printNote("Generating filters for " + element.toString());

			final var elementHibernateAccessorsName = element.toString() + "_";
			final var elementHibernateAccessors = processingEnv
				.getElementUtils()
				.getTypeElement(elementHibernateAccessorsName);

			if (elementHibernateAccessors == null) {
				processingEnv
					.getMessager()
					.printNote("Missing class " + elementHibernateAccessorsName);
				// Put element back into queue if it can't be processed at the moment.
				// At later point in time it may be possible to process it.
				elementsToProcess.add(element);
				continue;
			}

			final var annotation = element.getAnnotation(GenerateFilters.class);
			final var fieldsToSkip = new HashSet<String>(Arrays.asList(annotation.fieldsToIgnore()));

			try {
				generateFile(element, fieldsToSkip);
				processingEnv
					.getMessager()
					.printNote("Generated filters for " + element.toString());

			} catch (IOException e) {
				processingEnv
					.getMessager()
					.printWarning("Failed to generate filters " + e.getMessage(), element);
			}
		}

		return true;
	}

	private void generateFile(Element element, HashSet<String> fieldsToSkip) throws IOException {
		final var elementClassName = element.getSimpleName().toString();
		final var elementPackageName = element.getEnclosingElement().toString();

		final var className = elementClassName + "Filters";
		final var classFullName = elementPackageName + "." + className;

		processingEnv
			.getMessager()
			.printNote("Generating " + classFullName);

		final var sourceFile = processingEnv
			.getFiler()
			.createSourceFile(classFullName);

		try (final var writer = new PrintWriter(sourceFile.openWriter())) {
			writer.print("package ");
			writer.print(elementPackageName);
			writer.println(";");
			writer.println();

			writer.println("import com.example.filter.NumericValueFilter;");
			writer.println("import com.example.filter.StringValueFilter;");
			writer.println("import jakarta.validation.Valid;");
			writer.println("import jakarta.persistence.metamodel.SingularAttribute;");
			writer.println("import lombok.Getter;");
			writer.println("import lombok.Setter;");
			writer.println("import lombok.Builder;");
			writer.println("import lombok.NoArgsConstructor;");
			writer.println("import lombok.AllArgsConstructor;");
			writer.println("import org.springframework.data.jpa.domain.Specification;");
			writer.println();

			writer.println("@Getter");
			writer.println("@Setter");
			writer.println("@Builder");
			writer.println("@NoArgsConstructor");
			writer.println("@AllArgsConstructor");
			writer.print("public class ");
			writer.print(className);
			writer.println(" {");

			final var fieldsDescriptions = ElementFilter.fieldsIn(element.getEnclosedElements())
				.stream()
				.map(variableElement -> {
					final var fieldDescription = new FieldDescription();
					fieldDescription.type = variableElement.asType().toString();
					fieldDescription.name = variableElement.getSimpleName().toString();
					fieldDescription.filter = selectFilter(fieldDescription.type);
					return fieldDescription;
				})
				.filter(fieldDescription -> {
					if (fieldsToSkip.contains(fieldDescription.name)) {
						processingEnv
							.getMessager()
							.printNote("Skipped field " + fieldDescription.name);
						return false;
					}
					if (fieldDescription.filter == null) {
						processingEnv
							.getMessager()
							.printWarning("Unsupported type " + fieldDescription.type);
						return false;
					}

					return true;
				})
				.toList();

			if (fieldsDescriptions.isEmpty()) {
				processingEnv
					.getMessager()
					.printWarning("Generated filters are empty");
			}

			writeFields(writer, fieldsDescriptions);
			writer.println();

			writeIntoSpecification(writer, elementClassName, fieldsDescriptions);
			writer.println();

			writeNumericValueFilterFunction(writer, elementClassName);
			writer.println();

			writeStringValueFilterFunction(writer, elementClassName);
			writer.println();

			writer.println("}");
		}
	}

	private void writeFields(PrintWriter writer, List<FieldDescription> fieldsDescriptions) {
		for (final var fieldDescription : fieldsDescriptions) {
			writer.print("\t@Valid private ");
			writer.print(fieldDescription.filter);
			writer.print(" ");
			writer.print(fieldDescription.name);
			writer.println(";");
		}
	}

	private void writeIntoSpecification(PrintWriter writer, String elementClassName, List<FieldDescription> fieldsDescriptions) {
		writer.print("\tpublic Specification<");
		writer.print(elementClassName);
		writer.println("> intoSpecification() {");
		writer.println("\t\treturn Specification.allOf(");
		if (!fieldsDescriptions.isEmpty()) {
			FieldDescription fieldDescription = fieldsDescriptions.getFirst();
			writer.print("\t\t\tmapFilterToSpecification(");
			writer.print(elementClassName);
			writer.print("_.");
			writer.print(fieldDescription.name);
			writer.print(", ");
			writer.print(fieldDescription.name);
			writer.print(")");
			for (int i = 1; i < fieldsDescriptions.size(); ++i) {
				fieldDescription = fieldsDescriptions.get(i);
				writer.println(",");
				writer.print("\t\t\tmapFilterToSpecification(");
				writer.print(elementClassName);
				writer.print("_.");
				writer.print(fieldDescription.name);
				writer.print(", ");
				writer.print(fieldDescription.name);
				writer.print(")");
			}
		}
		writer.println();
		writer.println("\t\t);");
		writer.println("\t}");
	}

	private void writeNumericValueFilterFunction(PrintWriter writer, String elementClassName) {
		writer.print("\tprivate static <T extends Comparable<T>> Specification<");
		writer.print(elementClassName);
		writer.println("> mapFilterToSpecification(");
		writer.print("\t\tSingularAttribute<");
		writer.print(elementClassName);
		writer.println(", ? extends T> attribute,");
		writer.print("\t\t");
		writer.print(NumericValueFilter.class.getSimpleName());
		writer.println("<? extends T> filter");
		writer.println("\t) {");
		writer.println("\t\tif (filter == null) {");
		writer.println("\t\t\treturn (root, query, cb) -> cb.conjunction();");
		writer.println("\t\t}");
		writer.println();
		writer.println("\t\treturn switch (filter.getType()) {");
		writer.println("\t\t\tcase LESS          -> (root, query, cb) -> cb.lessThan(root.get(attribute), filter.getV1());");
		writer.println("\t\t\tcase LESS_EQUAL    -> (root, query, cb) -> cb.lessThanOrEqualTo(root.get(attribute), filter.getV1());");
		writer.println("\t\t\tcase EQUAL         -> (root, query, cb) -> cb.equal(root.get(attribute), filter.getV1());");
		writer.println("\t\t\tcase GREATER       -> (root, query, cb) -> cb.greaterThan(root.get(attribute), filter.getV1());");
		writer.println("\t\t\tcase GREATER_EQUAL -> (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(attribute), filter.getV1());");
		writer.println("\t\t\tcase BETWEEN       -> (root, query, cb) -> cb.between(root.get(attribute), filter.getV1(), filter.getV2());");
		writer.println("\t\t};");
		writer.println("\t}");
	}

	private void writeStringValueFilterFunction(PrintWriter writer, String elementClassName) {
		writer.print("\tprivate static <T extends Comparable<T>> Specification<");
		writer.print(elementClassName);
		writer.println("> mapFilterToSpecification(");
		writer.print("\t\tSingularAttribute<");
		writer.print(elementClassName);
		writer.println(", ? extends T> attribute,");
		writer.print("\t\t");
		writer.print(StringValueFilter.class.getSimpleName());
		writer.println(" filter");
		writer.println("\t) {");
		writer.println("\t\tif (filter == null) {");
		writer.println("\t\t\treturn (root, query, cb) -> cb.conjunction();");
		writer.println("\t\t}");
		writer.println();
		writer.println("\t\treturn switch (filter.getType()) {");
		writer.println("\t\t\tcase EQUAL             -> (root, query, cb) -> cb.equal(root.get(attribute), filter.getV());");
		writer.println("\t\t\tcase EQUAL_IGNORE_CASE -> (root, query, cb) -> cb.equal(");
		writer.println("\t\t\t\tcb.upper(");
		writer.println("\t\t\t\t\troot");
		writer.println("\t\t\t\t\t\t.get(attribute)");
		writer.println("\t\t\t\t\t\t.as(String.class)");
		writer.println("\t\t\t\t),");		
		writer.println("\t\t\t\tfilter.getV().toUpperCase()");
		writer.println("\t\t\t);");
		writer.println("\t\t};");
		writer.println("\t}");
	}

	private String selectFilter(String type) {
		switch (type) {
			case "java.lang.Integer":
			case "java.lang.Long":
			case "java.math.BigDecimal":
			case "java.time.LocalDate":
			case "java.time.LocalDateTime":
				return NumericValueFilter.class.getSimpleName() + "<" + type + ">";
			case "java.lang.String":
				return StringValueFilter.class.getSimpleName();
			default:
				return null;
		}
	}
    
	class FieldDescription {
		public String type;
		public String name;
		public String filter;
	}
}
