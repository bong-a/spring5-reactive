package org.rpis5.chapters.chapter_01.imperative;

import org.rpis5.chapters.chapter_01.commons.Input;
import org.rpis5.chapters.chapter_01.commons.Output;

public class BlockingShoppingCardService implements ShoppingCardService {

    @Override
    public Output calculate(Input value) {
        try {
            /**
             *  HTTP 요청이나 데이터베이스 쿼리와 같이 시간이 걸리는 I/O작업을 실행한다고 가정
             *  */
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new Output();
    }
}
