package com.qnxy.blog.core.ciphertext;

import com.qnxy.blog.core.CiphertextHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

/**
 * 获取 {@link CiphertextHandler} 工厂类
 *
 * @author Qnxy
 */
@Component
public class CiphertextHandlerFactory implements BeanFactoryAware {

    private BeanFactory beanFactory;


    public CiphertextHandler withCiphertextHandlerClass(Class<? extends CiphertextHandler> ciphertextHandlerClass) {
        return this.beanFactory.getBean(ciphertextHandlerClass);
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
