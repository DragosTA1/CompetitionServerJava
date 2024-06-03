module ro.mpp2024.network{
    requires ro.mpp2024.services;
    requires com.google.gson;
    requires ro.mpp2024.model;
    exports ro.mpp2024.network.utils;
    exports ro.mpp2024.network.jsonprotocol;
    exports ro.mpp2024.network.dto;
}