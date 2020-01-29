package org.rpis5.chapters.chapter_01.callbacks;

import org.rpis5.chapters.chapter_01.commons.Input;

public class OrdersService {
    private final ShoppingCardService shoppingCardService;

    public OrdersService(ShoppingCardService shoppingCardService) {
        this.shoppingCardService = shoppingCardService;
    }

    /**
     * 장점은 콜백함수를 통해 컴포넌트를 분리 -> shoppingCardService.calculate() 호출 후에 응답기다리지 않고
     * 즉시 다른 작업을 진행 할 수 있다.
     * 단점은 공유데이터변경 및 콜백지온을 피하기 위해 멀티스레딩을 잘 이해하고 있어야한다.
     * */
    void process() {
        Input input = new Input();
        shoppingCardService.calculate(input, output -> {
            System.out.println(shoppingCardService.getClass().getSimpleName() + " execution completed");
        });
        System.out.println(shoppingCardService.getClass().getSimpleName()+"'s OrderService process execution completed");
    }

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();

        OrdersService ordersServiceAsync = new OrdersService(new AsyncShoppingCardService());
        OrdersService ordersServiceSync = new OrdersService(new SyncShoppingCardService());

        ordersServiceAsync.process();
        ordersServiceAsync.process();
        ordersServiceSync.process();

        System.out.println("Total elapsed time in millis is : " + (System.currentTimeMillis() - start));

        Thread.sleep(1000);
    }
}
