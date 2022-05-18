package pl.aswit.theatre.util.feign;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import feign.InvocationHandlerFactory;
import feign.Target;

public class ValidationInvocationHandlerFactory implements InvocationHandlerFactory {

    private final Default defaultInvocationHandlerFactory;
    private final Validator javaxValidator;
    private Object targetTypeInstance;

    public ValidationInvocationHandlerFactory(Default defaultInvocationHandlerFactory, Validator javaxValidator) {
        this.defaultInvocationHandlerFactory = defaultInvocationHandlerFactory;
        this.javaxValidator = javaxValidator;
    }

    @Override
    public InvocationHandler create(Target target, Map<Method, MethodHandler> dispatch) {
        this.targetTypeInstance = createTargetInstance(target.type());

        for (final Map.Entry<Method, MethodHandler> entry : dispatch.entrySet()) {
            entry.setValue(new MethodHandlerDecorator(entry.getKey(), entry.getValue()));
        }

        return defaultInvocationHandlerFactory.create(target, dispatch);
    }

    private Object createTargetInstance(final Class<?> type) {
        return Proxy.newProxyInstance(
            type.getClassLoader(),
            new Class<?>[] { type },
            (o, method, objects) -> {
                if ("hashCode".equals(method.getName())) {
                    return hashCode();
                } else if ("toString".equals(method.getName())) {
                    return toString();
                } else {
                    return null;
                }
            }
        );
    }

    private class MethodHandlerDecorator implements MethodHandler {

        private final Method method;
        private final MethodHandler methodHandler;

        private MethodHandlerDecorator(final Method method, final MethodHandler methodHandler) {
            this.method = method;
            this.methodHandler = methodHandler;
        }

        @Override
        public Object invoke(final Object[] argv) throws Throwable {
            final Object response = methodHandler.invoke(argv);

            Set<ConstraintViolation<Object>> constraintViolationSet = javaxValidator.forExecutables().validateReturnValue(targetTypeInstance, method, response);
            if (!constraintViolationSet.isEmpty()) {
                throw new ConstraintViolationException(constraintViolationSet);
            }

            return response;
        }
    }
}
