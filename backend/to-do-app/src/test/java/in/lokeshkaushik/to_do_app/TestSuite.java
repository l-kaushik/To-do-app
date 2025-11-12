package in.lokeshkaushik.to_do_app;

import in.lokeshkaushik.to_do_app.controller.UserControllerIntegrationTest;
import in.lokeshkaushik.to_do_app.controller.UserControllerTest;
import in.lokeshkaushik.to_do_app.service.UserServiceIntegrationTest;
import in.lokeshkaushik.to_do_app.service.UserServiceTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        UserServiceTest.class,
        UserServiceIntegrationTest.class,
        UserControllerTest.class,
        UserControllerIntegrationTest.class
})
public class TestSuite {
}
