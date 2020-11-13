package com.derkovich.springdocuments.service.events;

import com.derkovich.springdocuments.service.dto.User;
import org.springframework.context.ApplicationEvent;

public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private User user;

    public OnRegistrationCompleteEvent(User user){
        super(user);
        System.out.println("EVENT INIT");
        this.user = user;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
