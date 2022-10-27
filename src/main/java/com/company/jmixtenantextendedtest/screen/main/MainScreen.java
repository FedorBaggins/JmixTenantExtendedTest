package com.company.jmixtenantextendedtest.screen.main;

import com.company.jmixtenantextendedtest.entity.User;
import io.jmix.core.DataManager;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.core.session.SessionData;
import io.jmix.multitenancy.core.TenantProvider;
import io.jmix.multitenancy.entity.Tenant;
import io.jmix.ui.ScreenTools;
import io.jmix.ui.component.*;
import io.jmix.ui.component.mainwindow.Drawer;
import io.jmix.ui.icon.JmixIcon;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.stream.Collectors;

@UiController("MainScreen")
@UiDescriptor("main-screen.xml")
@Route(path = "main", root = true)
public class MainScreen extends Screen implements Window.HasWorkArea {

    @Autowired
    private ScreenTools screenTools;

    @Autowired
    private AppWorkArea workArea;
    @Autowired
    private Drawer drawer;
    @Autowired
    private Button collapseDrawerButton;
    @Autowired
    private SessionData sessionData;
    @Autowired
    private ComboBox<String> tenantComboBox;
    @Autowired
    private DataManager dataManager;

    // меняем тенант вручную в ComboBox
    @Subscribe("tenantComboBox")
    public void onAccountComboBoxValueChange(HasValue.ValueChangeEvent<Tenant> event) {
        sessionData.setAttribute("tenant", event.getValue());
    }

    @Override
    public AppWorkArea getWorkArea() {
        return workArea;
    }

    @Subscribe("collapseDrawerButton")
    private void onCollapseDrawerButtonClick(Button.ClickEvent event) {
        drawer.toggle();
        if (drawer.isCollapsed()) {
            collapseDrawerButton.setIconFromSet(JmixIcon.CHEVRON_RIGHT);
        } else {
            collapseDrawerButton.setIconFromSet(JmixIcon.CHEVRON_LEFT);
        }
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        screenTools.openDefaultScreen(
                UiControllerUtils.getScreenContext(this).getScreens());

        screenTools.handleRedirect();
    }

//    заполняем ComboBox тенантами, которые мы можем выбирать в последующем
    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {

        var tenants = dataManager.load(Tenant.class).all().list().stream().map(Tenant::getTenantId).collect(Collectors.toList());

        var currentTenant = tenants.stream().findFirst().orElse(TenantProvider.NO_TENANT);
        tenantComboBox.setOptionsList(tenants);
        tenantComboBox.setValue(currentTenant);

        sessionData.setAttribute("tenant", currentTenant);
    }
}
