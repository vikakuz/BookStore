package ru.vsu.cs.app.bookstore.search_activty;

import java.io.Serializable;

public class MenuItem implements Serializable {
        private boolean value;
        private String onClick;

        public MenuItem(boolean value, String onClick) {
            this.value = value;
            this.onClick = onClick;
        }
    }