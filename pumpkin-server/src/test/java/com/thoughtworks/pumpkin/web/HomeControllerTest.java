package com.thoughtworks.pumpkin.web;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class HomeControllerTest {
    @Test
    public void shouldReturnIndex() {
        HomeController homeController = new HomeController();
        assertThat(homeController.index(), is("index"));
    }
}
