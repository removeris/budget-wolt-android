package com.example.budgetwolt_android_final.utilities;

public class Constants {

    public static final String IP_ADDRESS = "192.168.32.194";
    public static final String HOME_URL = "http://" + IP_ADDRESS + ":8080/";
    public static final String VALIDATE_LOGIN_URL = HOME_URL + "validateLogin";
    public static final String ALL_RESTAURANTS_URL = HOME_URL + "allRestaurants";
    public static final String UPDATE_USER_URL = HOME_URL + "updateUser";
    public static final String RESTAURANT_MENU_URL = HOME_URL + "getMenuRestaurant";
    public static final String CREATE_ORDER_URL = HOME_URL + "createOrder";
    public static final String ORDERS_BY_USER_URL = HOME_URL + "getOrderByUser";
    public static final String CHAT_BY_ORDER_URL = HOME_URL + "getMessagesForOrder";
    public static final String SEND_MESSAGE_URL = HOME_URL + "sendMessage";
    public static final String SUBMIT_RATING_URL = HOME_URL + "submitRating";
    public static final String INSERT_USER_URL = HOME_URL + "insertUser";
    public static final String INSERT_DRIVER_URL = HOME_URL + "insertDriver";
    public static final String AVAILABLE_ORDERS_URL = HOME_URL + "getDriverTasks";
}
