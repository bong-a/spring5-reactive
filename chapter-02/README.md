# Chapter 2. 스프링을 이용한 리액티브 프로그래밍 - 기본 개념

## 리액티브를 위한 스프링 프레임워크의 초기 해법

- 스프링 4.x의 ListenableFuture 클래스 -> 비동기 실행에 활용
- 스프링 4.x 중에 소수의 컴포넌트만이 비동기 실행을 위한 CompletableFutre 클래스를 지원

그럼에도 불구하고 스프링 프레임워크는 리액티브 애플리케이션을 구축하는데 유용한 인프라의 다른 부분을 제공하는데 그 중 일부를 알아 보자.

### 관찰자(Observer) 패턴

관찰자라고 불리는 자손의 리스트를 가지고 있는 주체(subject)가 필요하다.

subject는 일반적으로 자신의 메서드 중 하나를 호출해 관찰자에게 상태 변경을 알린다.

- 이 패턴은 이벤트 처리를 기반으로 시스템을 구현할 때 필수적이다.
- MVC 패턴의 중요한 부분이기도 하다. -> UI 라이브러리가 대부분 내부적으로 이 패턴을 사용

- 예시로 뉴스레터 구독을 들 수 있다.

- 런타임에 객체 사이에 일대다 의존성을 등록할 수 있다.
- 구성요소 구현 세부사항에 대해 알지 못해도 된다.(타입 안정성을 위해 관찰자가 수신이벤트의 타입을 인식할 수 는 있음)
- 이를 통해 활발히 상호작용하면서 결합도를 낮출 수 있다.
- 이런 유형의 통신은 단방향이며 효율적으로 이벤트를 배포하는데 도움이 된다.

- [RxJava에서 공부한 Observer 패턴]( [https://github.com/bong-a/rxjava/tree/master/src/main/java/com/study/rxjava/chapter3#311-%EC%98%B5%EC%A0%80%EB%B2%84-%ED%8C%A8%ED%84%B4](https://github.com/bong-a/rxjava/tree/master/src/main/java/com/study/rxjava/chapter3#311-옵저버-패턴) )

Obsever구현체가 Subject의 존재를 전혀 익식하지 못하게 할수도 있다.

이 경우에 Subject의 모든 인스턴스를 찾고 각 Subject 인스턴스에 Observer를 등록하는 역할을 담당할 세번째 컴포넌트가 필요할 수 도있다.

- 대표적인 예시로 DI 컨테이너가 있다. 
- DI컨테이너는 클래스패스를 검색해 @EventListener 어노테이션과 메서드 시그니처를 비교해 각 Observer의 인스턴스를 검색한 다음 발견된 컴포넌트를 Subject에 등록한다.

### 관찰자 패턴 소스 구현

[ConcreteObserver.java](./src/main/java/org/rpis5/chapters/chapter_02/observer/ConcreteSubject.java)

- 멀티스레드 시나리오에서 스레드 안정성을 유지하기 위해 업데이트 작업이 발생할 때마다 새 복사본을 생성하는 set 구현체인 CopyOnWriteArraySet을 사용 -> 그다지 효율적이지 않지만 합리적이다.

대기 시간이 상당히 긴 이벤트를 처리하는 관찰자가 많을 경우 추가적으로 스레드 할당이나 스레드 풀을 활용하여 병렬로 전달할 수 있다.

-> [ParallelSubject](./src/main/java/org/rpis5/chapters/chapter_02/observer/ParallelSubject.java)

- 이러한 개선은 자주 발생하는 비효율성 및 내재된 버그를 포함하는 파악하기 어려운 코드를 만드는 길일수도 있다.
- 스레드 풀 크기 제한 안하면 -> OOM 발생 가능성 있음
- 결과적으로 다중 스레드를 위한 관찰자 패턴이 필요할 때 검증된 라이브러리를 사용하는 것이 좋다.

java.util 패키지의 Observer 및 Observable 클래스

- JDK 1.0 부터 존재하던 오래된 클래스
- 자바 제네릭 이전에 도입되어있어기 떄문에 Object 타입을 사용, 타입 안전성이 보장되지 않음
- 멀티 스레드 환경에서 효율적이지 않음
- 자바 9에서 사용되지 않음



### @EventListener를 사용한 발행-구독 패턴

스프링 프레임워크는 관찰자 패턴을 자제적으로 구현함

이를 라이프 사이클 이벤트를 추적하는데 널리 사용함

스프링 4.2부터 이구현과 그에 수반되는 API가 확장돼 애플리케이션 이벤트외에 비즈니스 로직에서도 사용할 수 있음

- 이벤트 처리를 위한 @EventListener
- 이벤트 발행을 위한 ApplicationEventPublisher 클래스

위의 두 개는 관찰자 패턴의 변형으로 보일 수도 있지만 발행-구독 패턴을 구현한다는 것을 명확히 알 필요가 있다.

- 관찰자 패턴과 달리 게시자와 구독자는 서로 알 필요가 없다.
- 게시자와 구독자간의 간접적인 계층을 제공
- 구독자는 이벤트 채널은 알지만, 게시자가 누구인지 신경쓰지 않음
- 각 이벤트 채널에 몇 명의 게시자가 있을 수도 있음
- 이벤트 채널에서는 구독자에게 메세지 수신하기전에 필터링 작업 가능
- 토픽기반시스템 : 구독자는 관심 토픽에 게시된 모든 메시지 수진

### @EventListener 활용한 예제

- 방 안의 온도를 보여주는 웹 서비스
- 리액티브 디자인에 따라 만들어야 하므로 고전적인 풀링 모델을 사용 불가능
- 서버에서 클라이언트로 비동기 메세지 전달할 수 있는 웹소켓 & SSE 프로토콜 활용

[Temperature](./src/main/java/org/rpis5/chapters/chapter_02/pub_sub_app/Temperature.java)

[TemperatureSensor](./src/main/java/org/rpis5/chapters/chapter_02/pub_sub_app/TemperatureSensor.java)

[TemperatureController](./src/main/java/org/rpis5/chapters/chapter_02/pub_sub_app/TemperatureController.java)
```markdown

# 스프링 웹 MVC를 이용한 비동기 HTTP 통신

## 서블릿 3.0에서 추가된 비동기 지원 기능

해당 기능은 HTTP 요청을 처리하는 기능을 확장함

컨테이너 스레드를 사용하는 방식으로 구현됨

따라서 @Controller는 단일 타입 T 이외에도 Callable<T> 및 DeferredResult<T>를 반환할 수 있게 되었다.

- Callable<T> : 컨테이너 스레드 외부에서 실행 될 수 있지만, 여전히 블로킹 호출
- DeferredResult<T> : 컨테이너 스레드 외부에서도 비동기 응답을 생성하므로 이벤트 루프안에서도 사용할수 있다.

## 스프링 4.2에서의 ResponseBodyEmitter

DeferredResult<T>와 비슷하게 동작

ResponseBodyEmitter는 메시지 컨버터에 의해 개별적으로 만들어진 여러 개의 오브젝트를 전달하는 용도로 사용할 수 있다.

## SseEmitter

ResponseBodyEmitter를 상속, 하나의 수신요청에 대해 다수의 발신 메시지 보낼수 있음

## StreamingResponseBody

@Controller에서 반환활 때 데이터를 비동기적으로 보낼 수 있다.

서블릿 스레드를 차단하지 않으면서 큰파일을 스트리밍해야 하는 경우에 매우 유용
```

[Application](./src/main/java/org/rpis5/chapters/chapter_02/pub_sub_app/Application.java)

[index.html](./src/main/resources/static/index.html)



### 온도 서비스의 문제점

- 스프링에서 제공하는 발행-구독 구조 사용
  - 스프링 프레임워크에서 이 메커니즘은 처음에 생명주기 이벤트를 처리하기 위해 도입됨
  - 고부하 및 고성능 시나리오를 위하네 아님
  - 따라서 수백만개의 개별 스트림이 필요할 때, 문제가 발생 할 수 있음

- 비즈니스 로직을 정의하고 구현하기 위해 스프링 프레임워크의 내부 메커니즘을 사용한다는 점
  - 프레임워크의 사소한 변경으로 인해 프로그램의 안정성을 위협할 수 있음
  - 스프링 컨텍스트 로드하지 않고 비즈니스 로직을 단위테스트 하기 어려움
- 구성 요소간의 스트림 종료 및 오류 발생 알리기 위해 구현이 필요한데 이를 처리하기 어려움 
- 온도 이벤트를 비동기적으로 브로드캐스팅 하기 위해 스레드 풀 사용
  - 진정한 비동기적인 리액티브 접근에서는 필요 없는 일
- 클라이언트가 없을때도 이벤트 발생
  - 자원낭비 -> 실제로 하드웨어와 통신하는 경우에는 하드웨어 수명을 단축 시킬 수 있음

이러한 문제를 해결하기 위해 이 목적만을 위해 설계된 리액티브 라이브러리가 필요하다. 



## 리액티브 프레임워크 RxJava

RxJava라이브러리는 Reactive Extension(ReactiveX)의 자바 구현체

Reactive Extension는 동기식 또는 비동기식 스트림과 관계없이 명령형 언어를 이용해 데이터 스트림을 조작 할 수 있는 일련의 도구

### 관찰자 + 반복자 = 리액티브 스트림

기존의 관찰자 패턴은 

- 무한한 데이터 스트림이 가능하지만 
- 데이터 스트림의 끝을 알리는 기능이 없다.
- 컨슈머가 준비하기 전에 프로듀서가 이벤트를 생성하기도 함

동기적으로 작동할때는  이 경우에 반복자 패턴이 있음. 하나씩 항목을 검색하기 위해 next메서드 제공

이를 관찰자 패턴에 의한 비동기 실행과 혼합

```java
public interface RxObserver<T> {
   void onNext(T next);
   void onComplete();
   void onError(Exception e);
}
```

- onComplete() 메서드를 통해 스트림의 끝을 알림
- onError() 메서드를 통해 오류 알림

이 인터페이스는 리액티브 스트림의 몬든 컴포넌트 사이에 데이터가 흐르는 방법을 정의한다.

결과적으로

- Oberbable = Subject = 이벤트 발생시킬 때 이벤트 소스 역할 수행
- Subscriber = Observer 인터페이스를 구현 = 이벤트를 소비



### 스트림의 생산과 소비

- java 8 람다
- Observable 및 Subscriber 인스턴스 생성하기 위해 많은 유연성 제공
  - 요소를 직접 등록 가능
  - 배열 사용 가능
  - Iterable 컬렉션 이용해 Observable 인스턴스 만들 수 있음
  - Callable 및 Future 활용 가능

### 비동기 시퀀스 생성하기

- interval() 메서드 등을 이용해 주기적으로 비동기 이벤트 시퀀스 생성 가능
- 이벤트가 생성되는 것과는 별개의 스레드에서 사용된다
- 그래서 메인 스레드 바로 종료되면 메세지 못받음

### 스트림 변환과 마블 다이어그램

- Observable과 구독자만으로도 다양한 워크플로우 구현가능하지만
- RxJava의 모든 기능은 연산자에 의해 구현된다.
- 마블 다이어그램은 스트림 변환을 시각적으로 표현한다.

#### 연산자 

- [Map 연산자]( https://github.com/bong-a/rxjava/tree/master/src/main/java/com/study/rxjava/chapter4#421-map )
- [Filter 연산자]( https://github.com/bong-a/rxjava/tree/master/src/main/java/com/study/rxjava/chapter4#431-filter )
- [Count 연산자]( https://github.com/bong-a/rxjava/tree/master/src/main/java/com/study/rxjava/chapter4#455-count )
- [Zip 연산자]( https://github.com/bong-a/rxjava/tree/master/src/main/java/com/study/rxjava/chapter4#445-zip--zipwith)

### RxJava 사용의 전제 조건 및 이점

구독자가 관찰 가능한 스트림에 가입한 후 비동기적으로 이벤트를 생성해 프로세스를 시작한다는 핵심 개념을 동일

- 프로듀서-컨슈머 관계를 해지할 수 있는 채널이 일반적으로 존재

  - 이러한 접근 방식은 매우 융통성있으며 생성 및 소비되는 이벤트 양을 제어할 수 있게 해줌

  - 데이터 작성 시에만 필요하고 이후에는 사용되지 않는 CPU 사용량을 줄일 수 있다

  - 예시 : 메모리 검색 엔진 서비스 

    * Iterator 패턴 활용 : 데이터 반환을 기다릴 때 클라이언트의 스레드 차단, 고성능 응용프로그램에 적합하지 않음

    * CompletableFuture 활용: 콜백 호출하기 때문에 검색 요청에 영향 없음. 하지만 결과가 한 번에 전체를 반환하거나 아무것도 반환하지 않는 방식으로만 동작

    * Rxjava 활용 : 비동기 처리 및 이후에 수신하는 각 이벤트 대응 능력 갖춰짐 

      ​						구독 취소 가능 

      ​						-> 응답성을 높여줌

- 기존의 동기 방식 코드를 비동기 워크플로로 래핑 가능
  - 느린 Callable에 대한 실행 스레드 관리를 위해 [subscribeOn 연산자]( [https://github.com/bong-a/rxjava/tree/master/src/main/java/com/study/rxjava/chapter3#321-rxjava%EC%97%90%EC%84%9C-%EB%B9%84%EB%8F%99%EA%B8%B0-%EC%B2%98%EB%A6%AC](https://github.com/bong-a/rxjava/tree/master/src/main/java/com/study/rxjava/chapter3#321-rxjava에서-비동기-처리) )를 사용할 수 있음

- RxJava, 리액티브 프로그래밍에서는 불변 객체를 사용해야 한다.
  - 이는 함수형 프로그래밍의 핵심 원리 중 하나 = ' 객체는 일단 만들어지면 변경되지 않는다'

### RxJava를 이용해 온도 서비스 다시 만들기

- 더 이상 스프링 프레임 워크에 의존하지 않음
- RxJava 기반 구현은 클라이언트가 없을 때는 온도 센서를 탐색하는 오버헤드가 없다
  - 발행-구독 예제를 다시 보면 이러한 속성이 없으며 더 제한적임

### 리액티브 프로그래밍의 (간단한) 역사

-  https://blog.canapio.com/78 

### 리액티브의 전망

- RatPack
- Retrofit
- Vert.x