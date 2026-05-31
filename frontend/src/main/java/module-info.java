module com.openlib.market.frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;

    opens com.openlib.market.frontend to javafx.fxml;
    opens com.openlib.market.frontend.controller to javafx.fxml;
    opens com.openlib.market.frontend.model to com.fasterxml.jackson.databind;
    opens com.openlib.market.frontend.service to com.fasterxml.jackson.databind;

    exports com.openlib.market.frontend;
    exports com.openlib.market.frontend.app;
    exports com.openlib.market.frontend.session;
    exports com.openlib.market.frontend.http;
    exports com.openlib.market.frontend.model;
    exports com.openlib.market.frontend.service;
    exports com.openlib.market.frontend.controller;
}
