module org.fissore.jrecordbindtests {

  requires org.fissore.jrecordbind;
  requires java.xml.bind;

  // the following `exports` go here because maven-jaxb2-plugin generates sources
  // into generated-sources rather than generated-test-sources
  exports org.jrecordbind.schemas.jrb.imported to org.fissore.jrecordbind;
  exports org.jrecordbind.schemas.jrb.importing to org.fissore.jrecordbind;
  exports org.jrecordbind.schemas.jrb._enum to org.fissore.jrecordbind;
  exports org.jrecordbind.schemas.jrb.choice_dynamic_length_line_separator to org.fissore.jrecordbind;
  exports org.jrecordbind.schemas.jrb.choice to org.fissore.jrecordbind;
  exports org.jrecordbind.schemas.jrb.dynamic_length to org.fissore.jrecordbind;
  exports org.jrecordbind.schemas.jrb.enum_with_restrictions to org.fissore.jrecordbind;
  exports org.jrecordbind.schemas.jrb.head_and_tail_use_same_record_id to org.fissore.jrecordbind;
  exports org.jrecordbind.schemas.jrb.hierarchical to org.fissore.jrecordbind;
  exports org.jrecordbind.schemas.jrb.multi_row to org.fissore.jrecordbind;
  exports org.jrecordbind.schemas.jrb.only_sub_records to org.fissore.jrecordbind;
  exports org.jrecordbind.schemas.jrb.simple to org.fissore.jrecordbind;
  exports org.jrecordbind.schemas.jrb.simple_fixed_length_line_separator_and_delimiter to org.fissore.jrecordbind;
  exports org.jrecordbind.schemas.jrb.unhappy.imported.double_main_element to org.fissore.jrecordbind;
  exports org.jrecordbind.schemas.jrb.unhappy.importing.double_main_element to org.fissore.jrecordbind;
  exports org.jrecordbind.schemas.jrb.unhappy.mixed_properties_and_sub_records to org.fissore.jrecordbind;

}
