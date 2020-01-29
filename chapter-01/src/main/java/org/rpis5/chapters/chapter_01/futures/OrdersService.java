package org.rpis5.chapters.chapter_01.futures;

import org.rpis5.chapters.chapter_01.commons.Input;
import org.rpis5.chapters.chapter_01.commons.Output;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class OrdersService {
    private final ShoppingCardService shoppingCardService;

    public OrdersService(ShoppingCardService shoppingCardService) {
        this.shoppingCardService = shoppingCardService;
    }

    /**
     * 콜백지옥을 피할 수 있다.
     * 하지만 원하는 결과를 얻으려면 현재 스레드를 차단하고 확장성을 현저히 저하시키는 외부 실행과 동기화해야 한다.
     */
    void process() {
        Input input = new Input();
        Future<Output> result = shoppingCardService.calculate(input);

        System.out.println(shoppingCardService.getClass().getSimpleName() + " execution completed");

        try {
            result.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        OrdersService ordersService1 = new OrdersService(new FutureShoppingCardService());

        ordersService1.process();
        ordersService1.process();

        System.out.println("Total elapsed time in millis is : " + (System.currentTimeMillis() - start));
    }
}
