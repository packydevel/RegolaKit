package org.regola.roo.addon.regola.activerecord;

import static org.springframework.roo.model.JdkJavaType.LIST;


import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.roo.classpath.PhysicalTypeIdentifierNamingUtils;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.details.FieldMetadata;
import org.springframework.roo.classpath.details.FieldMetadataBuilder;
import org.springframework.roo.classpath.details.ItdTypeDetailsBuilder;
import org.springframework.roo.classpath.details.MethodMetadata;
import org.springframework.roo.classpath.details.MethodMetadataBuilder;
import org.springframework.roo.classpath.details.annotations.AnnotatedJavaType;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadataBuilder;
import org.springframework.roo.classpath.itd.AbstractItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.classpath.itd.InvocableMemberBodyBuilder;
import org.springframework.roo.metadata.MetadataIdentificationUtils;
import org.springframework.roo.model.DataType;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.LogicalPath;

/**
 * This type produces metadata for a new ITD. It uses an {@link ItdTypeDetailsBuilder} provided by 
 * {@link AbstractItdTypeDetailsProvidingMetadataItem} to register a field in the ITD and a new method.
 * 
 * @since 1.1.0
 */
public class ActiveRecordMetadata extends AbstractItdTypeDetailsProvidingMetadataItem {

    // Constants
    private static final String PROVIDES_TYPE_STRING = ActiveRecordMetadata.class.getName();
    private static final String PROVIDES_TYPE = MetadataIdentificationUtils.create(PROVIDES_TYPE_STRING);

    public static final String getMetadataIdentiferType() {
        return PROVIDES_TYPE;
    }
    
    public static final String createIdentifier(JavaType javaType, LogicalPath path) {
        return PhysicalTypeIdentifierNamingUtils.createIdentifier(PROVIDES_TYPE_STRING, javaType, path);
    }

    public static final JavaType getJavaType(String metadataIdentificationString) {
        return PhysicalTypeIdentifierNamingUtils.getJavaType(PROVIDES_TYPE_STRING, metadataIdentificationString);
    }

    public static final LogicalPath getPath(String metadataIdentificationString) {
        return PhysicalTypeIdentifierNamingUtils.getPath(PROVIDES_TYPE_STRING, metadataIdentificationString);
    }

    public static boolean isValid(String metadataIdentificationString) {
        return PhysicalTypeIdentifierNamingUtils.isValid(PROVIDES_TYPE_STRING, metadataIdentificationString);
    }
    
    public ActiveRecordMetadata(String identifier, JavaType aspectName, PhysicalTypeMetadata governorPhysicalTypeMetadata) {
        super(identifier, aspectName, governorPhysicalTypeMetadata);
        Validate.isTrue(isValid(identifier), "Metadata identification string '" + identifier + "' does not appear to be a valid");

        // Adding patternParser field
        builder.addField(getPatternParser());
        
       // Adding find method
        builder.addMethod(getFindMethod());
        
        // Adding count method
        builder.addMethod(getCountMethod());
        
        // Adding queryList method
        builder.addMethod(getQueryListMethod());
        
     // Adding queryListPaged method
        builder.addMethod(getQueryListPagedMethod());
        
        // Adding querySingle
        builder.addMethod(getQuerySingleMethod());
        
        // Adding queryUpdate
        builder.addMethod(getQueryUpdateMethod());
        
        // Adding imports
        builder.getImportRegistrationResolver().addImport(new JavaType("org.regola.filter.impl.DefaultPatternParser"));
        builder.getImportRegistrationResolver().addImport(new JavaType("org.regola.filter.criteria.jpa.JpaQueryBuilder"));
        builder.getImportRegistrationResolver().addImport(new JavaType("javax.persistence.TypedQuery"));
        builder.getImportRegistrationResolver().addImport(new JavaType("javax.persistence.Query"));
        // Create a representation of the desired output ITD
        itdTypeDetails = builder.build();
    }
    
    /**
     * Create metadata for a field definition. 
     *
     * @return a FieldMetadata object
     */
    private FieldMetadata getPatternParser() {
        // Note private fields are private to the ITD, not the target type, this is undesirable if a dependent method is pushed in to the target type
        int modifier = Modifier.STATIC | Modifier.PRIVATE;
        
        // Using the FieldMetadataBuilder to create the field definition. 
        final FieldMetadataBuilder fieldBuilder = new FieldMetadataBuilder(getId(), // Metadata ID provided by supertype
            modifier, // Using package protection rather than private
            //new ArrayList<AnnotationMetadataBuilder>(), // No annotations for this field
            new JavaSymbolName("patternParser"), // Field name
            new  JavaType("org.regola.filter.ModelPatternParser"), "new DefaultPatternParser()"); // Field type
        
        return fieldBuilder.build(); // Build and return a FieldMetadata instance
    }
    

    
    private MethodMetadata getFindMethod() {

    	// Specify the desired method name
        JavaSymbolName methodName = new JavaSymbolName("find");
                
        // Define method parameter types (none in this case)
        List<AnnotatedJavaType> parameterTypes = new ArrayList<AnnotatedJavaType>();
        parameterTypes.add(AnnotatedJavaType.convertFromJavaType(new  JavaType("org.regola.model.ModelPattern")));
        
        // Define method parameter names (none in this case)
        List<JavaSymbolName> parameterNames = new ArrayList<JavaSymbolName>();
        parameterNames.add(new JavaSymbolName("pattern"));
        
        // Check if a method with the same signature already exists in the target type
        final MethodMetadata method = methodExists(methodName, parameterTypes);
        if (method != null) {
            // If it already exists, just return the method and omit its generation via the ITD
            return method;
        }
        
        // Define method annotations
        final List<AnnotationMetadataBuilder> annotations = new ArrayList<AnnotationMetadataBuilder>();
        final AnnotationMetadataBuilder annotationBuilder = new AnnotationMetadataBuilder(
                new JavaType("java.lang.SuppressWarnings"));
        annotationBuilder.addStringAttribute("value","unchecked");
        annotations.add(annotationBuilder);
       
        // Define method throws types (none in this case)
        List<JavaType> throwsTypes = new ArrayList<JavaType>();
        
        // Create the method body
        InvocableMemberBodyBuilder bodyBuilder = new InvocableMemberBodyBuilder();
        bodyBuilder.appendFormalLine("JpaQueryBuilder criteriaBuilder = new JpaQueryBuilder(" +
        destination.getSimpleTypeName() + ".class, entityManager());");
        bodyBuilder.appendFormalLine("patternParser.createQuery(criteriaBuilder, pattern);");
        bodyBuilder.appendFormalLine("return criteriaBuilder.getQuery().getResultList();");
        
        // Create the return type
        final JavaType returnType = new JavaType(
                LIST.getFullyQualifiedTypeName(), 0, DataType.TYPE, null,
                Arrays.asList(destination));
        
        // Use the MethodMetadataBuilder for easy creation of MethodMetadata
        MethodMetadataBuilder methodBuilder = new MethodMetadataBuilder(getId(), Modifier.PUBLIC | Modifier.STATIC, methodName, 
        		returnType, parameterTypes, parameterNames, bodyBuilder);
        methodBuilder.setAnnotations(annotations);
        methodBuilder.setThrowsTypes(throwsTypes);
        
        return methodBuilder.build(); // Build and return a MethodMetadata instance
    }
    
    private MethodMetadata getQueryListMethod() {

    	// Specify the desired method name
        JavaSymbolName methodName = new JavaSymbolName("queryList");
                
        
        List<AnnotatedJavaType> parameterTypes = new ArrayList<AnnotatedJavaType>();
        parameterTypes.add(AnnotatedJavaType.convertFromJavaType(JavaType.STRING));
        parameterTypes.add(AnnotatedJavaType.convertFromJavaType(JavaType.OBJECT));
        
        // Define method parameter names 
        List<JavaSymbolName> parameterNames = new ArrayList<JavaSymbolName>();
        parameterNames.add(new JavaSymbolName("jpql"));
        parameterNames.add(new JavaSymbolName("...params"));
        
        // Check if a method with the same signature already exists in the target type
        final MethodMetadata method = methodExists(methodName, parameterTypes);
        if (method != null) {
            // If it already exists, just return the method and omit its generation via the ITD
            return method;
        }
        
        // Define method annotations
        final List<AnnotationMetadataBuilder> annotations = new ArrayList<AnnotationMetadataBuilder>();
        final AnnotationMetadataBuilder annotationBuilder = new AnnotationMetadataBuilder(
                new JavaType("java.lang.SuppressWarnings"));
        annotationBuilder.addStringAttribute("value","unchecked");
        //annotations.add(annotationBuilder);
       
        // Define method throws types (none in this case)
        List<JavaType> throwsTypes = new ArrayList<JavaType>();
        
        // Create the method body
        InvocableMemberBodyBuilder bodyBuilder = new InvocableMemberBodyBuilder();
        
        bodyBuilder.appendFormalLine(
        		String.format("TypedQuery<%s> query =  entityManager().createQuery(jpql, %s.class);",destination.getSimpleTypeName(), destination.getSimpleTypeName()));
        
        bodyBuilder.appendFormalLine("int index = 0;");
        bodyBuilder.appendFormalLine("for (Object param : params) query.setParameter(++index, param);");
        bodyBuilder.appendFormalLine("return query.getResultList();");
        
        // Create the return type
        final JavaType returnType = new JavaType(
                LIST.getFullyQualifiedTypeName(), 0, DataType.TYPE, null,
                Arrays.asList(destination));
        
        // Use the MethodMetadataBuilder for easy creation of MethodMetadata
        MethodMetadataBuilder methodBuilder = new MethodMetadataBuilder(getId(), Modifier.PUBLIC | Modifier.STATIC, methodName, 
        		returnType, parameterTypes, parameterNames, bodyBuilder);
        methodBuilder.setAnnotations(annotations);
        methodBuilder.setThrowsTypes(throwsTypes);
        
        return methodBuilder.build(); // Build and return a MethodMetadata instance
    }

    private MethodMetadata getQueryListPagedMethod() {

    	// Specify the desired method name
        JavaSymbolName methodName = new JavaSymbolName("queryListPaged");
                
        
        List<AnnotatedJavaType> parameterTypes = new ArrayList<AnnotatedJavaType>();
        parameterTypes.add(AnnotatedJavaType.convertFromJavaType(JavaType.STRING));
        parameterTypes.add(AnnotatedJavaType.convertFromJavaType(JavaType.INT_PRIMITIVE));
        parameterTypes.add(AnnotatedJavaType.convertFromJavaType(JavaType.INT_PRIMITIVE));
        parameterTypes.add(AnnotatedJavaType.convertFromJavaType(JavaType.OBJECT));
        
        // Define method parameter names 
        List<JavaSymbolName> parameterNames = new ArrayList<JavaSymbolName>();
        parameterNames.add(new JavaSymbolName("jpql"));
        parameterNames.add(new JavaSymbolName("first"));
        parameterNames.add(new JavaSymbolName("max"));
        parameterNames.add(new JavaSymbolName("...params"));
        
        // Check if a method with the same signature already exists in the target type
        final MethodMetadata method = methodExists(methodName, parameterTypes);
        if (method != null) {
            // If it already exists, just return the method and omit its generation via the ITD
            return method;
        }
        
        // Define method annotations
        final List<AnnotationMetadataBuilder> annotations = new ArrayList<AnnotationMetadataBuilder>();
        final AnnotationMetadataBuilder annotationBuilder = new AnnotationMetadataBuilder(
                new JavaType("java.lang.SuppressWarnings"));
        annotationBuilder.addStringAttribute("value","unchecked");
        //annotations.add(annotationBuilder);
       
        // Define method throws types (none in this case)
        List<JavaType> throwsTypes = new ArrayList<JavaType>();
        
        // Create the method body
        InvocableMemberBodyBuilder bodyBuilder = new InvocableMemberBodyBuilder();
        
        bodyBuilder.appendFormalLine(
        		String.format("TypedQuery<%s> query =  entityManager().createQuery(jpql, %s.class);",destination.getSimpleTypeName(), destination.getSimpleTypeName()));
        
        bodyBuilder.appendFormalLine("int index = 0;");
        bodyBuilder.appendFormalLine("for (Object param : params) query.setParameter(++index, param);");
        bodyBuilder.appendFormalLine("query.setFirstResult(first);");
        bodyBuilder.appendFormalLine("query.setMaxResults(max);");
        bodyBuilder.appendFormalLine("return query.getResultList();");
        
        // Create the return type
        final JavaType returnType = new JavaType(
                LIST.getFullyQualifiedTypeName(), 0, DataType.TYPE, null,
                Arrays.asList(destination));
        
        // Use the MethodMetadataBuilder for easy creation of MethodMetadata
        MethodMetadataBuilder methodBuilder = new MethodMetadataBuilder(getId(), Modifier.PUBLIC | Modifier.STATIC, methodName, 
        		returnType, parameterTypes, parameterNames, bodyBuilder);
        methodBuilder.setAnnotations(annotations);
        methodBuilder.setThrowsTypes(throwsTypes);
        
        return methodBuilder.build(); // Build and return a MethodMetadata instance
    }

    
    
    private MethodMetadata getQueryUpdateMethod() {

    	// Specify the desired method name
        JavaSymbolName methodName = new JavaSymbolName("queryUpdate");
                
        
        List<AnnotatedJavaType> parameterTypes = new ArrayList<AnnotatedJavaType>();
        parameterTypes.add(AnnotatedJavaType.convertFromJavaType(JavaType.STRING));
        parameterTypes.add(AnnotatedJavaType.convertFromJavaType(JavaType.OBJECT));
        
        // Define method parameter names 
        List<JavaSymbolName> parameterNames = new ArrayList<JavaSymbolName>();
        parameterNames.add(new JavaSymbolName("jpql"));
        parameterNames.add(new JavaSymbolName("...params"));
        
        // Check if a method with the same signature already exists in the target type
        final MethodMetadata method = methodExists(methodName, parameterTypes);
        if (method != null) {
            // If it already exists, just return the method and omit its generation via the ITD
            return method;
        }
        
        // Define method annotations
        final List<AnnotationMetadataBuilder> annotations = new ArrayList<AnnotationMetadataBuilder>();
        final AnnotationMetadataBuilder annotationBuilder = new AnnotationMetadataBuilder(
                new JavaType("java.lang.SuppressWarnings"));
        annotationBuilder.addStringAttribute("value","unchecked");
        //annotations.add(annotationBuilder);
       
        // Define method throws types (none in this case)
        List<JavaType> throwsTypes = new ArrayList<JavaType>();
        
        // Create the method body
        InvocableMemberBodyBuilder bodyBuilder = new InvocableMemberBodyBuilder();
        
        bodyBuilder.appendFormalLine("Query query =  entityManager().createQuery(jpql);");
        bodyBuilder.appendFormalLine("int index = 0;");
        bodyBuilder.appendFormalLine("for (Object param : params) query.setParameter(++index, param);");
        bodyBuilder.appendFormalLine("return query.executeUpdate();");
        
        // Create the return type
        final JavaType returnType = JavaType.INT_PRIMITIVE;
        
        // Use the MethodMetadataBuilder for easy creation of MethodMetadata
        MethodMetadataBuilder methodBuilder = new MethodMetadataBuilder(getId(), Modifier.PUBLIC | Modifier.STATIC, methodName, 
        		returnType, parameterTypes, parameterNames, bodyBuilder);
        methodBuilder.setAnnotations(annotations);
        methodBuilder.setThrowsTypes(throwsTypes);
        
        return methodBuilder.build(); // Build and return a MethodMetadata instance
    }

    
    
    private MethodMetadata getQuerySingleMethod() {

    	// Specify the desired method name
        JavaSymbolName methodName = new JavaSymbolName("querySingle");
                
        
        List<AnnotatedJavaType> parameterTypes = new ArrayList<AnnotatedJavaType>();
        parameterTypes.add(AnnotatedJavaType.convertFromJavaType(JavaType.STRING));
        parameterTypes.add(AnnotatedJavaType.convertFromJavaType(JavaType.OBJECT));
        
        // Define method parameter names 
        List<JavaSymbolName> parameterNames = new ArrayList<JavaSymbolName>();
        parameterNames.add(new JavaSymbolName("jpql"));
        parameterNames.add(new JavaSymbolName("...params"));
        
        // Check if a method with the same signature already exists in the target type
        final MethodMetadata method = methodExists(methodName, parameterTypes);
        if (method != null) {
            // If it already exists, just return the method and omit its generation via the ITD
            return method;
        }
        
        // Define method annotations
        final List<AnnotationMetadataBuilder> annotations = new ArrayList<AnnotationMetadataBuilder>();
        final AnnotationMetadataBuilder annotationBuilder = new AnnotationMetadataBuilder(
                new JavaType("java.lang.SuppressWarnings"));
        annotationBuilder.addStringAttribute("value","unchecked");
        //annotations.add(annotationBuilder);
       
        // Define method throws types (none in this case)
        List<JavaType> throwsTypes = new ArrayList<JavaType>();
        
        // Create the method body
        InvocableMemberBodyBuilder bodyBuilder = new InvocableMemberBodyBuilder();
        
        bodyBuilder.appendFormalLine(
        		String.format("TypedQuery<%s> query =  entityManager().createQuery(jpql, %s.class);",destination.getSimpleTypeName(), destination.getSimpleTypeName()));
        
        bodyBuilder.appendFormalLine("int index = 0;");
        bodyBuilder.appendFormalLine("for (Object param : params) query.setParameter(++index, param);");
        bodyBuilder.appendFormalLine("return query.getSingleResult();");
        
        // Create the return type
        final JavaType returnType = destination;
        
        // Use the MethodMetadataBuilder for easy creation of MethodMetadata
        MethodMetadataBuilder methodBuilder = new MethodMetadataBuilder(getId(), Modifier.PUBLIC | Modifier.STATIC, methodName, 
        		returnType, parameterTypes, parameterNames, bodyBuilder);
        methodBuilder.setAnnotations(annotations);
        methodBuilder.setThrowsTypes(throwsTypes);
        
        return methodBuilder.build(); // Build and return a MethodMetadata instance
    }

    private MethodMetadata getQueryListMethodOld() {

    	// Specify the desired method name
        JavaSymbolName methodName = new JavaSymbolName("queryList");
                
        
        List<AnnotatedJavaType> parameterTypes = new ArrayList<AnnotatedJavaType>();
        parameterTypes.add(AnnotatedJavaType.convertFromJavaType(JavaType.STRING));
        
        // Define method parameter names 
        List<JavaSymbolName> parameterNames = new ArrayList<JavaSymbolName>();
        parameterNames.add(new JavaSymbolName("jpql"));
        
        
        
        // Check if a method with the same signature already exists in the target type
        final MethodMetadata method = methodExists(methodName, parameterTypes);
        if (method != null) {
            // If it already exists, just return the method and omit its generation via the ITD
            return method;
        }
        
        // Define method annotations
        final List<AnnotationMetadataBuilder> annotations = new ArrayList<AnnotationMetadataBuilder>();
        final AnnotationMetadataBuilder annotationBuilder = new AnnotationMetadataBuilder(
                new JavaType("java.lang.SuppressWarnings"));
        annotationBuilder.addStringAttribute("value","unchecked");
        //annotations.add(annotationBuilder);
       
        // Define method throws types (none in this case)
        List<JavaType> throwsTypes = new ArrayList<JavaType>();
        
        // Create the method body
        InvocableMemberBodyBuilder bodyBuilder = new InvocableMemberBodyBuilder();
        
        bodyBuilder.appendFormalLine(
        		String.format("return entityManager().createQuery(jpql, %s.class).getResultList();", destination.getSimpleTypeName()));
        
        // Create the return type
        final JavaType returnType = new JavaType(
                LIST.getFullyQualifiedTypeName(), 0, DataType.TYPE, null,
                Arrays.asList(destination));
        
        // Use the MethodMetadataBuilder for easy creation of MethodMetadata
        MethodMetadataBuilder methodBuilder = new MethodMetadataBuilder(getId(), Modifier.PUBLIC | Modifier.STATIC, methodName, 
        		returnType, parameterTypes, parameterNames, bodyBuilder);
        methodBuilder.setAnnotations(annotations);
        methodBuilder.setThrowsTypes(throwsTypes);
        
        return methodBuilder.build(); // Build and return a MethodMetadata instance
    }

    
    
    private MethodMetadata getCountMethod() {

    	// Specify the desired method name
        JavaSymbolName methodName = new JavaSymbolName("count");
                
        // Define method parameter types (none in this case)
        List<AnnotatedJavaType> parameterTypes = new ArrayList<AnnotatedJavaType>();
        parameterTypes.add(AnnotatedJavaType.convertFromJavaType(new  JavaType("org.regola.model.ModelPattern")));
        
        // Define method parameter names (none in this case)
        List<JavaSymbolName> parameterNames = new ArrayList<JavaSymbolName>();
        parameterNames.add(new JavaSymbolName("pattern"));
        
        // Check if a method with the same signature already exists in the target type
        final MethodMetadata method = methodExists(methodName, parameterTypes);
        if (method != null) {
            // If it already exists, just return the method and omit its generation via the ITD
            return method;
        }
        
        // Define method annotations
        final List<AnnotationMetadataBuilder> annotations = new ArrayList<AnnotationMetadataBuilder>();
       
        // Define method throws types (none in this case)
        List<JavaType> throwsTypes = new ArrayList<JavaType>();
        
        // Create the method body
        InvocableMemberBodyBuilder bodyBuilder = new InvocableMemberBodyBuilder();
        bodyBuilder.appendFormalLine("JpaQueryBuilder criteriaBuilder = new JpaQueryBuilder(" +
        destination.getSimpleTypeName() + ".class, entityManager());");
        //bodyBuilder.appendFormalLine("patternParser.createQuery(criteriaBuilder, pattern);");
        bodyBuilder.appendFormalLine("patternParser.createCountQuery(criteriaBuilder, pattern);");

        bodyBuilder.appendFormalLine("return ((Number) criteriaBuilder.getQuery().getSingleResult()).intValue();");
        
        // Create the return type
        final JavaType returnType  = JavaType.INT_PRIMITIVE;
        
        // Use the MethodMetadataBuilder for easy creation of MethodMetadata
        MethodMetadataBuilder methodBuilder = new MethodMetadataBuilder(getId(), Modifier.PUBLIC | Modifier.STATIC , methodName, 
        		returnType, parameterTypes, parameterNames, bodyBuilder);
        methodBuilder.setAnnotations(annotations);
        methodBuilder.setThrowsTypes(throwsTypes);
        
        return methodBuilder.build(); // Build and return a MethodMetadata instance
    }

    
    private MethodMetadata methodExists(JavaSymbolName methodName, List<AnnotatedJavaType> paramTypes) {
        // We have no access to method parameter information, so we scan by name alone and treat any match as authoritative
        // We do not scan the superclass, as the caller is expected to know we'll only scan the current class
        for (MethodMetadata method : governorTypeDetails.getDeclaredMethods()) {
            if (method.getMethodName().equals(methodName) && method.getParameterTypes().equals(paramTypes)) {
                // Found a method of the expected name; we won't check method parameters though
                return method;
            }
        }
        return null;
    }
    
    // Typically, no changes are required beyond this point
    
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("identifier", getId());
        builder.append("valid", valid);
        builder.append("aspectName", aspectName);
        builder.append("destinationType", destination);
        builder.append("governor", governorPhysicalTypeMetadata.getId());
        builder.append("itdTypeDetails", itdTypeDetails);
        return builder.toString();
    }
}
