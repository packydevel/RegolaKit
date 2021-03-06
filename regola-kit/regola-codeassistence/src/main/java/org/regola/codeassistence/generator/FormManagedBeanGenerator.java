package org.regola.codeassistence.generator;

import freemarker.template.Template;
import org.regola.codeassistence.Environment;
import org.regola.codeassistence.VariablesBuilder;


public class FormManagedBeanGenerator extends AbstractGenerator
{
	private static final String LIST_TEMPLATE = "formMBean.ftl";
	private static final String FACES_CONTEXT_TEMPLATE = "formMBeanFacesContextBean.ftl";
	private static final String SPRING_BEAN_TEMPLATE = "formMBeanSpringBean.ftl";
	private static final String VALIDATION_TEMPLATE = "formMBeanValidation.ftl";
        
	public void generate(Environment env, VariablesBuilder pb)
	{
		Template template = env.getTemplate(LIST_TEMPLATE);	  
		env.addJavaSource((String) pb.getParameters().get("mbean_package")
				, (String) pb.getParameters().get("mbean_form_name")
				, template
				, pb.getParameters());
		
		template = env.getTemplate(SPRING_BEAN_TEMPLATE);
		String beanId = VariablesBuilder.camelNotation((String)pb.getParameters().get("mbean_form_name"));
		env.addSpringBeansToApplicationContext( beanId, template, pb.getParameters());
		
                
        template = env.getTemplate(VALIDATION_TEMPLATE);
		env.addResource( pb.getParameters().get("mbean_form_name") + "Amendments.xml", template,  pb.getParameters());
		
	}

	public boolean existsArtifact(Environment env, VariablesBuilder pb) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public FormManagedBeanGenerator() {
		name =  "form-handler";
	}

	public String getDisplayName() {

		return "JSF detail managed bean";
	}
	
	public String getDescription() {
		return "Produces a JSF detail managed bean.";

	}
}
