package com.sparta.endpointtesting.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApiConfig {

    private static final Properties props = new Properties();

    static {
        try (InputStream stream = ApiConfig.class
                .getClassLoader()
                .getResourceAsStream("config.properties"))
        {
            if (stream != null) {
                props.load(stream);
            } else {
                throw new IOException("Unable to find config.properties");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getBaseUri() {
        return props.getProperty("automationexercise.base_uri");
    }

    public static String getProductsList() {
        return props.getProperty("automationexercise.api_path.all_products_list");
    }

    public static String getBrandsList() {
        return props.getProperty("automationexercise.api_path.all_brands_list");
    }

    public static String getSearchProducts() {
        return props.getProperty("automationexercise.api_path.search_product");
    }

    public static String getVerifyLogin() {
        return props.getProperty("automationexercise.api_path.verify_login");
    }

    public static String getCreateAccount() {
        return props.getProperty("automationexercise.api_path.create_account");
    }

    public static String getDeleteAccount() {
        return props.getProperty("automationexercise.api_path.delete_account");
    }

    public static String getUpdateAccount() {
        return props.getProperty("automationexercise.api_path.update_account");
    }

    public static String getUserDetailByEmail() {
        return props.getProperty("automationexercise.api_path.get_user_detail_by_email");
    }
}
