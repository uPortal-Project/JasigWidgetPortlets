package org.jasig.portlet.widget.service;

import groovy.lang.GroovyClassLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.codehaus.groovy.control.CompilationFailedException;
import org.junit.Before;
import org.junit.Test;

public class DictionaryParsingServiceTest {

	IDictionaryParsingService dictService;
	
	@Before
	public void setUp() throws CompilationFailedException, IOException, InstantiationException, IllegalAccessException { 
        // load up our Groovy service
        GroovyClassLoader gcl = new GroovyClassLoader();
        @SuppressWarnings("unchecked")
        Class clazz = gcl.parseClass(new File("src/main/resources/org/jasig/portlet/widget/service/DictionaryParsingServiceImpl.groovy"));
        Object aScript = clazz.newInstance();
        dictService = (IDictionaryParsingService) aScript;
	}
	
	@Test
	public void testGetDefintion() throws FileNotFoundException {
		InputStream stream = new FileInputStream("src/test/resources/TestDefinition.xml");
		String def = dictService.getDefinitionFromXml(stream);
		
		assert def.startsWith("test\n                n 1: any standardized procedure");
		assert def.endsWith("7: undergo a test; \"She doesn't test well\"\n            ");
	}

}
