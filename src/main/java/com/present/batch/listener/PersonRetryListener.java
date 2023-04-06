package com.present.batch.listener;

import com.present.batch.processor.PersonItemProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.listener.RetryListenerSupport;

@Slf4j(topic = "PersonRetryListener")
public class PersonRetryListener extends RetryListenerSupport {

    @Override
    public void onError(RetryContext retryContext, RetryCallback retryCallback,
        Throwable throwable) {

        log.info("Retry Count: " + retryContext.getRetryCount() + ", Counter Value: " + PersonItemProcessor.counter);
    }

}
