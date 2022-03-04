module GameLogic {
    exports controller;
    exports domain;
    exports opponent;
    exports opponent.default_opponents;
    exports persistence;
    opens opponent;
    requires java.sql;
    //requires org.junit.platform.commons;
}
