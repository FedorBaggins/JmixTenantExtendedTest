package com.company.jmixtenantextendedtest.screen.testentity;

import io.jmix.ui.screen.*;
import com.company.jmixtenantextendedtest.entity.TestEntity;

@UiController("TestEntity.browse")
@UiDescriptor("test-entity-browse.xml")
@LookupComponent("testEntitiesTable")
public class TestEntityBrowse extends StandardLookup<TestEntity> {
}