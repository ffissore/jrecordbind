package it.assist.jrecordbind.classloader.domain;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

    private final static QName _Main_QNAME = new QName("http://schemas.assist.it/jrb/domain", "main");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.assist_si.schemas.jrb.it.assist.jrecordbind.classloader.domain.simple
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ClassLoaderRecord }
     * 
     */
    public ClassLoaderRecord createSimpleRecord() {
        return new ClassLoaderRecord();
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link ClassLoaderRecord }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.assist.it/jrb/domain", name = "main")
    public JAXBElement<ClassLoaderRecord> createMain(ClassLoaderRecord value) {
        return new JAXBElement<ClassLoaderRecord>(_Main_QNAME, ClassLoaderRecord.class, null, value);
    }

}
