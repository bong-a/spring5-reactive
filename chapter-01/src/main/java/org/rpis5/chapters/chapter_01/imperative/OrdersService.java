package org.rpis5.chapters.chapter_01.imperative;

import org.rpis5.chapters.chapter_01.commons.Input;
import org.rpis5.chapters.chapter_01.commons.Output;

public class OrdersService {

    private final ShoppingCardService scService;

    public OrdersService(ShoppingCardService scService) {
        this.scService = scService;
    }

    /**
     * 단순히 OrderService를 실행하는 경우에도 ShoppingCardService의 실행 결과와 강결합된다.
     * ShoppingCardService가 요청을 처리하는 동안 다른 작업을 실행 할 수 없다.
     * OrderService에서 별도의 독립적인 처리 실행하려면 추가 스레드를 할당해야한다. -> 이는 낭비일 수도 있다.
     * 결과적으로, 리액티브 시스템 관점에서 그런식의 동작은 허용되지 않는다.
     * 이 문제를 해결하기 위해 콜백, Future
     * */
    void process() {
        Input input = new Input();
        Output output = scService.calculate(input);
        System.out.println(scService.getClass().getSimpleName() + " execution completed");
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        new OrdersService(new BlockingShoppingCardService()).process();
        new OrdersService(new BlockingShoppingCardService()).process();

        System.out.println("Total elapsed time in millis is : " + (System.currentTimeMillis() - start));
    }
}
