package com.company.jmixtenantextendedtest.screen.testentity;

import com.company.jmixtenantextendedtest.entity.TestEntity;
import com.company.jmixtenantextendedtest.entity.User;
import io.jmix.core.DataManager;
import io.jmix.ui.component.ComboBox;
import io.jmix.ui.component.SuggestionField;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@UiController("TestEntity.edit")
@UiDescriptor("test-entity-edit.xml")
@EditedEntityContainer("testEntityDc")
public class TestEntityEdit extends StandardEditor<TestEntity> {

    @Autowired
    protected SuggestionField<String> userField;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private ComboBox<User> userCombo;

    //SuggestionField выдает ошибку java.lang.IllegalStateException: No thread-bound request found:...
    @Install(to = "userField", subject = "searchExecutor")
    protected List<User> countryFieldSearchExecutor(String searchString,
                                                    Map<String, Object> searchParams) {
        return dataManager.load(User.class)
                .all()
                .list();
    }

    //ComboBox трабатывает нормально
    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        userCombo.setOptionsList(dataManager.load(User.class).all()
                .list());
    }

}