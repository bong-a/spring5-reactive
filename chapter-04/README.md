# 리액터 프로젝트 - 리액티브 앱의 기초

## 리액터 프로젝트의 역사와 배경 

### 리액터 프로젝트 버전 1.x

- 대용량 데이터 응용 프로그램 개발 단순화하는 것이 목표

- 처음부터 비동기 논블로깅 처리를 지원

- 리액터 패턴,함수형 프로그래밍,리액티브 프로그래밍과 같은 메세지 처리에 대한 모범 사례 통한한 것

  - 리액터 패턴 : 비동기 이벤트 처리 및 동기 처리에 도움이 되는 행위 패턴

    모든 이벤트가 큐에 추가되고 이벤트는 나중에 별도의 스레드에 의해 처리됨

    이벤트는 모든 관련 컴포넌트(이벤트 핸들러)로 발송되고 동기적으로 처리됨

- 그 당시 이벤트를 빠른 솟도로 처리하기에 충분
- 스프링 프레임워크와 완벽한 통합가능
- 스프링 + 네티와의 결합을 통해 비동기 및 논블로킹 메시지 처리를 제공하는 시스템 개발 가능

#### 단점

1. 배압 기능 부재
   - 프로듀서 스레드 차단하거나 이벤트 생략하는 것 외에는 다른 배압 제어 방법 제공 X
2. 오류 처리가 복잡

### 리액터 프로젝트 버전 2.x

- 리액트 스프림의 첫번째 시도
- 이벤트 버스 및 스크림 기능을 별도의 모듈로 추출
- 새로운 리액터 스트림 라이브러리가 리액티브 스트림 스펙을 완벽하게 준수하도록 핵심 모듈 다시 설계
- 새로운 리액터API는 자바 컬렉션API와 쉽게 통합할 수 있음
- 배압관리,스레드 처리,복원력 지원 등이 다양한 기능 추가

### 리액터 프로젝트 버전 3.x

RxJava와 리액터프로젝트의 아이어와 경험을 reactive-stream-commons라는 라이브러리로 압축

이는 최종적으로 리액터 2.5의 기초가 되고 리액터 3.x가 되었다.

- RxJava가 자바 6을 대상으로 했다면 리액터 3는 자바 8을 기준으로 선택함
- 스프링프레임워크 5이 리액티브적인 변형을 담당

## 리액터 프로젝트의 용어와 API

리액터 프로젝트는 비동기 파이프라인을 구축할 때 콜백 지옥과 깊게 중첩된 코드를 생략하는 목적으로 설계됐다.

라이브러리 기본 목표 : 가독성 높이고, 조합성을 추가

리액터 API는 연산자를 연결해서 사용하는 것을 권장

리액터프로젝트의 오류 처리 연산자는 매우 유연하지만 복원력 있는 코드를 작성할 수 있게 해줌

배압은 핵심 요소

### 리액티브 타입 : Flux & Mono

Publisher의 구현체

Flux

- 0,1 또는 여러 요소 생성할 수 있는 일반저긴 리액티브 스트림을 정의
- 무한한 양의 요소를 만들 수 있음

Mono

- 최대 하나의 요소를 생성할 수 있는 스트림 정의
- 버퍼 중복과 값비싼 동기화 작업을 생략
- 많은 리액티브 연산자 제공
- 더 큰 규모의 리액티브 워크플로와 완벽하게 통합 할 수  있음
- 클라이언트에게 작업이 완료됐음을 알리는데 사용할 수 있음

Flux와 Mono는 서로 쉽게 변환할 수 있음

- Flux<T>.collectionList() -> Mono<List<T>>를 반환
- Mono<T>.flux() -> Flux<T>를 반환

### 연산자를 이용해 리액티브 시퀀스 변환하기

- 리액티브 시퀀스의 원소 매핑하기: map

- 리액티브 시퀀스 필터링하기 : filter, ignoreElements, take, takeLast, takeUntil, elementAt, skip, takeUntilOther

  ##### ignoreElements

  - Mono<T> 반환하고 어떤 원소도 통과시키지 않는다. 
  - 결과 시퀀스는 원본 시퀀스가 종료된 후에 종료 된다.

![img](https://projectreactor.io/docs/core/release/api/reactor/core/publisher/doc-files/marbles/ignoreElementsForMono.svg)

##### 	takeUntilOther, skipUntilOther

- ​	특정 스트림에서 메세지가 도착할 때 까지 원소를 건너뛰거나 가져올 수 있다.

![img](https://projectreactor.io/docs/core/release/api/reactor/core/publisher/doc-files/marbles/takeUntilOtherForMono.svg)

- 리액티브 시퀀스 수집하기 : Flux.collectList(), Flux.collectSortedList(), collectMap,collectMultimap,collect,distinct,distinctUntilChanged

  ##### collectMultimap

  - Map<K, Collection<T>>로 변환

![img](https://projectreactor.io/docs/core/release/api/reactor/core/publisher/doc-files/marbles/collectMultiMapWithKeyExtractor.svg)

- 스트림의 원소 줄이기 : count, all, any, hasElement, hasElements, sort, reduce, scan, then, thenMany, thenEmpty

  ##### then, thenMany, thenEmpty 

  - 들어오는 원소를 무시하고 완료 또는 오류 신호만 보냄

  - 상위 스트림이 완료될 때 동시에 완료됨

  - 상위 스트림 처리가 완료되는 즉시 새 스트림을 기동하는데 유용

    ![img](https://projectreactor.io/docs/core/release/api/reactor/core/publisher/doc-files/marbles/thenForFlux.svg)

- 리액티브 스트림 조합하기 : concat, merge, zip combineLatest

- 스트리 내의 원소 일괄 처리하기 

  - List와 같은 컨테이너를 이용한 버퍼링

  - Flux<Flux<T>>와 같은 형태로 스트림을 스트림하는 윈도우잉

  - Flux<GroupFlux<K,T>> 유형의 스트림으로 그룹화

  - flatMap

  - concatMap

  - flatMapSequential

    ![img](https://projectreactor.io/docs/core/release/api/reactor/core/publisher/doc-files/marbles/flatMapSequentialWithConcurrency.svg)

  - flatMapDelayError,flatMapSequentialDelayError,concatMapDelayError

  - concatMapIterable

- 샘플링하기 : sample, smapleTimeout

- 리액티브 시퀀스를 블로킹 구조로 전환하기 : toIterable, toStream, blockFirst, blockLast
- 시퀀스를 처리하는 동안 처리 내역 살펴보기 : doOnNext, doOnComplete, doOnSubscribe, doOnTerminate,doOnEach
- 데이터와 시그널 변환하기 : materialize, dematerialize

### 코드를 통해 스트림 만들기

- push
- create
- generate
- 일회성 리소스를 리액티브 스트림에 배치 : using, usingWhen

### 에러 처리하기

> onError 시그널은 리액티브 스트림 스펙의 필수 요소

- onError 시그널에 대한 핸들러를 정의하지 않으면 UnsupportedOperationException 발생

- onError 발생하면 스트림 종료로 정의 -> 시그널 받으면 시퀀스 실행 중지

- 이 시점에서 전략을 적용해 다른 방식으로 대응 가능

  - onErrorReturn : 예외 발생시 지정한 값으로 대체

  - onErrorResume : 예외를 catch하고 대체 워크플로를 실행

  - onErrorMap : 예외를 catch하고 상황을 더 잘 나타내는 다른 예외로 변환

  - retry : 오류 발생시 소스 리액티브 시퀀스를 다시 구독(무한대 가능)

  - retryBackoff : 백오프 알고리즘 지원해 재시도할 때마다 대기 시간 증가

    

  - defaultIfEmpty : 빈 스트림 대신 기본값을 반환

  - switchIfEmpty : 빈 스트림 대신 완전히 다른 리액티브 스트림을 반환

  - timeout : 작업대기 시간을 제한하고 TimeoutException 발생

### 배압다루기

- #### onBackpressureBuffer

  ![img](https://projectreactor.io/docs/core/release/api/reactor/core/publisher/doc-files/marbles/onBackpressureBuffer.svg)

- #### onBackpressureDrop

  ![img](https://projectreactor.io/docs/core/release/api/reactor/core/publisher/doc-files/marbles/onBackpressureDrop.svg)

- #### onBackpressureLatest

  ![img](https://projectreactor.io/docs/core/release/api/reactor/core/publisher/doc-files/marbles/onBackpressureLatest.svg)

- #### onBackpressureError

  ![img](https://projectreactor.io/docs/core/release/api/reactor/core/publisher/doc-files/marbles/onBackpressureError.svg)

#### 배압 관리하는 또 다른 방법 : 속도 제한 기술

- limitRate

  ![img](https://projectreactor.io/docs/core/release/api/reactor/core/publisher/doc-files/marbles/limitRate.svg)

- limitRequest

  ![img](https://projectreactor.io/docs/core/release/api/reactor/core/publisher/doc-files/marbles/limitRequest.svg)

### Hot 스트림과 Cold 스트림

콜드 퍼블리셔 : 구독자가 나타날때마다 모든 시퀀스 데이터가 생성되는 방식

- 구독자 없이 데이터 생성 X

핫 퍼블리셔 : 구독자 존재 여부에 의존 X -> 첫 구독자가 구독시작 전에 원소 만들어 냄 -> 이전에 생성된 값 보내지 않고 새로운  값만 보낼수 있음

- 예시 : 데이터 방송 시나리오

> 콜드 퍼블리셔 -> 핫 퍼블리셔 전환 가능

##### Flux

- publish
- cache
- share : 콜드 퍼블리셔 -> 핫 퍼블리셔로 변환, 구독자가 각 신규 구독자에게 이벤트를 전파하는 방식
- transform : 서로 다른 위치에서 동일한 순서의 연산자를 사용할 때 활용 (=리액터의 composer 연산자)

### 리액터 프로젝트 테스트 및 디버깅하기

> Hooks.onOperatorDebug();

- 조립할 모든 스트림에 대해 스택 트레이스를 수집하기 시작
- 비용이 많이 듦

> Flux,Mono 유형은 log 메서드 제공

- 연산자를 통과하는 모든 신호를 기록

### 리액터 추가 기능

리액터 애드온 프로젝트(https://github.com/reactor/reactor-addons)

- reactor-adapter : RxJava2 리액티브 타입 및 스케줄러에 대한 어댑터 제공
- reactor-logback : 고속의 비동기 로깅 제공
- ~~reactor-extra : 고급 기능을 위한 여러 추가 유틸리티~~

Reactor RabbitMQ

리액터 카프카 모듈

리액터 네티

### 리액티브 스트림의 생명 주기

리액터에서 멀티스레딩이 작동하는 방법과 내부 최적화가 구현되는 방법을 이해하기 위해서는 먼저 리액터에서 리액티브 타입의 수명 주기를 이해해야 한다.

#### 조립 단계

> 리액티브 라이브러리에서 실행 흐름을 작성하는 프로세스를 조립이라 한다.

리액터는 복잡한 처리 흐름을 구현할 수 있는 연쇄형 API 제공

빌더 API처럼 보임

빌더패턴은

- 가변적인 객체 생성
- 다른 객체 생성하기 위해 build()와 같은 최종함수 호출

리액터 API는

- 불변성을 제공
- 적용된 가각의 연산자가 새로운 객체를 생성



리액터 빌더API가 없는 경우의 조립 방식

```java
Flux<Integer> sourceFlux = new FluxArray(1,20,300,4000);
Flux<String> mapFlux = new FluxMap(sourceFlux, String::valueOf);
Flux<String> filterFlux = new FluxFilter(mapFlux, s -> s.length()>1)
```

```java
FluxFilter(
	FluxMap(
		FluxArray(1,2,3,40,500,6000)
	)
)
```



> 스트립 수명주기에서 조립단계가 중요한 이유는 스트림의 타입을 확인해 연산자를 서로 바꿀 수 있기 때문

concatWith -> concatWith -> concatWith 연산자의 시퀀스는 하나의 연결로 쉽게 압축 할 수 있다.

```java
/**
* Concatenate emissions of this {@link Flux} with the provided {@link Publisher} (no interleave).
* <p>
* <img class="marble" src="https://raw.githubusercontent.com/reactor/reactor-core/v3.1.3.RELEASE/src/docs/marble/concat.png"
* alt="">
*
* @param other the {@link Publisher} sequence to concat after this {@link Flux}
*
* @return a concatenated {@link Flux}
*/
public final Flux<T> concatWith(Publisher<? extends T> other) {
    if (this instanceof FluxConcatArray) {
        @SuppressWarnings({ "unchecked" })
        FluxConcatArray<T> fluxConcatArray = (FluxConcatArray<T>) this;
		// 매개변수가 FluxConcatArray인 경우에는 FluxConcatArray(FluxConcatArray(FluxA,FluxB),FluxC)로 동작하는 것이 아니라 하나의 FluxConcatArray(FluxA,FluxB,FluxC)를 만들어 전체적인성능을 향상시킨다.
        return fluxConcatArray.concatAdditionalSourceLast(other);
    }
    return concat(this, other);
}
```

> 조립 단계에서 스트림에 몇 가지 훅을 사용하면 디버깅이나 스크림 모니터링 중에 유용한 로깅을 축적,메트립 수깁 또는 기타 중요한 기능을 사용할 수 있다.

##### 정리

- 스트림 구성을 조작
- 리액티브 시스템을 구축하는 데 필수적인 디버깅 최적화나 모니터링, 더 나은 스트림 전달을 위한 다양한 기술을 적용할 수 있는 단계

#### 구독 단계

> 구독은 특정 Publisher를 구독할 때 발생한다.

예시

```java
Flux<Integer> sourceFlux = new FluxArray(1,20,300,4000);
Flux<String> mapFlux = new FluxMap(sourceFlux, String::valueOf);
Flux<String> filterFlux = new FluxFilter(mapFlux, s -> s.length()>1)

filterFlux.subscribe();
```

- 실행 플로를 만들기 위해 내부적으로 Publisher를 다른 Publisher에게 전달

- 따라서 일련의 Publisher 체인이 있다고 할 수 있다.

- 최상위 래퍼를 구독하면 해당 체인에 대한 구독 프로세스가 시작된다.

- 다음 코드는 구독 단계동안 Subscriber 체인을 통해 Subscriber가 전파되는 방식을 보여준다.

  ```java
  filterFlux.subscribe(Subscriber){
  	mapFlux.subscribe(new FilterSubscriber(Subscriber)){
  		arrayFlux.subscribe(new MapSubscriber(FilterSubscriber(Subscriber))){
  			//여기에서 실제 데이터를 송신하기 시작한다
  		}
  	}
  }
  ```

  이 코드는 조립된 Flux 내부에서 구독 단계동안 발생하는 상황을 보여준다.

  filterFlux.subscribe메서드를 실행하면 각 내부 Publisher에 대한 subscribe 메서드가 실행된다.

  주석이 있는 행에서 실행이 끝나고 나면 내부에서 다음과 같이 연결된 Subscriber 시퀀스가 존재하게 된다.

  ```java
  ArraySubscriber(
  	MapSubscriber(
  		FilterSubscriber(
  			Subscriber
  		)
  	)
  )
  ```

  조립된 Flux와 달리 Subscriber 피라미드 맨위에 ArraySubscriber 래퍼가 있다. 

  Flux 피라미드의 경우에는 FluxArray가 중간에 존재한다.

> 구독단계가 중요한 이유는 이 단계에서 조립단계와 동일한 최적화를 수행할 수 있기 때문이다.
>
> 또 다른 중요한 점은 리액터에서 멀티 스레딩을 지원하는 일부 연산자는 구독이 발생하는 작업자를 변경할 수 있다는 점이다.

#### 런타임 단계

- 이 단계에서 게시자와 구독자 간에 실제 신호가 교환된다.
- 게시자와 구독자가 교환하는 처음 두 신호는 `onSubscribe`와 `request`이다.

`onSubscribe` 메서드는 최상위 소스에서 호출한다.

아래 예제는 ArraySubscriber이다. 구독자는 메서드를 호출해 구독을 시작한다.

```java
MapSubscriber(FilterSubscriber(Subscriber)).onSubscribe(
	new ArraySubsription()
){
	FilterSubscriber(Subscriber).onSubscribe(
		new MapSubscription(ArraySubscription(...))
	){
        Subscriber.onSubscriber(
        	FilterSubscription(MapSubscription(ArraySubscription(...)))
        ){
         	//여기에 요청 데이터를 기술한다.   
        }
	}
}
```

구독이 모든 구독자 체인을 통과하고, 체인에 포함된 각 구독자가 지정된 구독을 자신의 표현으로 래핑하면 최종적으로 다음 코드와 같이 Subscription 래퍼의 피라미드를 얻는다.

```java
FilterSubscription(
	MapSubscription(
		ArraySubscription()
	)
)
```

마지막으로, 마지막 구독자가 구독 체인에 대한 정보를 수신하고 메세지 수신을 시작하려면 Subscription#request 메서드를 호출해 전송을 시작해야 한다.

다음 코드는 request 프로세스가 어떻게 보이는지 보여준다.

```java
FilterSubscription(MapSubscription(ArraySubscription(...)))
	.request(10){
		MapSubscription(ArraySubscription(...))
			.request(10){
				ArraySubscription(...)
					.request(10){
						// 데이터 전송 시작
					}
			}
	}
```

모든 구독자가 요청한 수요를 통과하고 ArraySubscription이 이를 수신하고 나면 ArrayFlux는 데이터를 MapSubscriber(FilterSubscriber(Subscriber)) 체인으로 보내기 시작한다.

다음은 모든 구독자를 통해 데이터를 보내는 프로세스를 설명하는 코드다.

```java
ArraySubscription.request(10){
	MapSubscriber(FilterSubscriber(Subscriber)).onNext(1){
		//데이터 변환 로직을 작성한다.
		FilterSubscriber(Subscriber).onNext("1"){
			//필터처리
			//원소가 일치하지 않으면
			//추가 데이터 요청
			MapSubscription(ArraySubscription(...)).request(1){...}
		}
	}
	
	MapSubscriber(FilterSubscriber(Subscriber)).onNext(20){
		//데이터 변환 로직을 작성한다.
		FilterSubscriber(Subscriber).onNext("20"){
			//필터처리
			//원소가 일치하면 
			//다운스트림 구독자에게 전송
			Subscriber.onNext("20"){...}
		}
	}
}
```

런타임 중에 데이터는 소스로부터 각 Subscriber 체인을 거쳐 단계마다 다른 기능을 실행한다.

> 런타임 단계가 중요한 이유는 런타임 중에 신호교환량을 줄이기 위한 최적화를 적용할 수 있기 때문이다.
>
> 예를 들어 Subscription#request 호출 횟수를 줄임으로써 스트림의 성능을 향상시킬 수 있다.



다음 절에서는 리액터가 각각의 리액티브 스트림을 어떨게 효율적인 구현했는지 확인하기 위해 스트림 수명 주기별 단계를 사용할 것이다.

### 리액터에서 스레드 스케줄링 모델

리액터가 멀티스레딩 실행을 위해 제공하는 연산자와 연산자 사이의 차이점을 알아보자

다른 워커로 실행을 전환할 수 있는 네 가지 연산자가 있다.

#### 1. publishOn 연산자

런타임 실행의 일부를 지정된 워커로 이동할 수 있게 해준다.

> 리액터는 런타임에 데이터를 처리할 워커를 지정하기 위해 Scheduler 개념 도입
>
> Scheduler는 리액터프로젝트에서 워커 또는 워커 풀을 나타내는 인터페이스이다.

```java
Scheduler scherduler = ...;
Flux.range(0.100)
	.map(String::valueOf)
	.filter(s -> s.length() > 1)
	//여기까지 메인 스레드
	.publishOn*schdeuler)
	//여기 이후로 스케줄러 스레드
	.map(this::calculateHash)
	.map(this::doBusinessLogic)
	subscribe()
```

publishOn연산자는 런타임 실행에 초점을 맞춘다.

내부적으적으로 전용 워커가 메세지를 하나씩 처리할 수 있도록 새로운 원소를 제공하는 큐를 가지고 있다.

작업이 별도의 스레드에서 실행 중이므로 비동기 영역에 의해 실행 플로가 분할돼 있다.

여기서는 플로의 두 부분을 독립적으로 처리했다

중요한 점은 리액티브 스트림의 모든 원소는 하나씩 처리되므로 항상 모든 이벤트에 순서를 엄격하게 정의할 수 있다는 것이다.

이 속성을 직렬성이라고도 한다.

즉, 원소가 publishOn에 오면 쿠에 추가되고 차례가 되면 큐에서 꺼내서 처리한다. 하나의 작업자만 큐를 처리하므로 원소의 순서는 항상 예측가능하다.

##### publishOn 연산자를 이용한 병렬 처리

publishOn은 리액티브 스트림의 원소를 동시에 처리하지 못하는 것처럼 보인다. 그럼에도 불구하고 리액터 프로젝트는 publishOn연산자를 사용함으로써 미세한 규모의 조정 및 처리 흐름의 병렬처리를 가능하게 해서 리액티브 프로그래밍 패러다임에 부합하고 있다. 

#### 2. SubscribeOn 연산자

구독 체인에서 워커의 작업 위치를 변경 할 수 있다.

이 연산자는 함수를 실행해 스트림 소스를 만들 때 유용하게 사용할 수 있다.

일반적으로 이러한 실행은 구독 시간에 수행되므로 subscribe 메서드를 실행하기 위한 데이터 원천 소스를 제공하는 함수가 호출된다.

예제 : Mono.fromCallabe을 사용해 정보를 제공하는 방법을 보여주는 예제

```java
ObjectMapper objectMapper = ...
String json = "{\"color\":\"BLACK\",\"type\":\"BMW\"}";
Mono.fromCallable(()->objectMapper.readValue(json,Car.class))
//Mono를 생성하고 실행결과를 각 구독자에게 전달
//Callable 인스턴스는 Subscribe메서드를 호출할 때 실행되므로 내부적으로 Mono.fromCallable은 다음과 같은 작업을 수행한다. 
...
 
```

```java
@Override
public void subscribe(CoreSubscriber<? super T> actual) {
    Operators.MonoSubscriber<T, T>
        sds = new Operators.MonoSubscriber<>(actual);

    actual.onSubscribe(sds);

    if (sds.isCancelled()) {
        return;
    }

    try {
        T t = callable.call();
        if (t == null) {
            sds.onComplete();
        }
        else {
            sds.complete(t);
        }
    }
    catch (Throwable e) {
        actual.onError(Operators.onOperatorError(e, actual.currentContext()));
    }

}
```

subscribe메서드에서 callable이 실행된다. 이것은 publishOn을 사용해 Callable이 실행될 워커를 변경할 수 있음을 알수 있다. -> subscribe메서드가 런타임 던계이니까 publishOn을 사용해 실행될 워커 변경할 수 있다.

다행히도 subscribeOn을 사용하면 수독을 수행할 워커를 지정할 수 있다.

```java
Scheduler scheduler = ...;
Mono.fromCallable(...)
.subscribeOn(schedluer)
.subscribe
```

내부적으로 subscribeOn은 부모 Publisher에 대한 구독을 Runnable 안에서 실행한다.

예제에서 이 Runnable은 Scheduler타입의 Scheduler 인스턴스다. subscribeOn과 publishOn의 실행 모델을 비교하면 다음과 같다.

------

Mono.fromCallable(...)

↑ 스레드A             ↓ 스레드 A

.subscribeOn(...)

↑ 메인스레드        ↓ 스레드 A

.filter(...)

↑ 메인스레드        ↓ 스레드 A

.publishOn(...)

↑ 메인스레드        ↓ 스레드 B

.map(...)

------

subscribeOn은 구독시간워커와 함께 런타임 워커를 부분적으로 지정할 수 있다.

subscribe 메서드의 실행 예약과 함께 Subscription.request() 메서드에 대한 각 호출을 예약하므로 Scheduler 인스턴스에 의해 지정된 워커에서 실행된다.

리액티브 스트림 스펙에 따르면 Publisher는 호출된 스레드에서 데이터를 보내기 시작할 수 있으므로 후속 Subscriber.onNext()는 초기 Subscription.request() 호출과 동일한 스레드에서 호출된다.

반대로 publishOn은 다운스트림에 대해서만 실행 동작을 지정할 수 있으며 업스트림 실행에는 영향을 미치지 않는다.

#### 3. parallel 연산자

> 하위 스트림에 대한 플로 분할과 분할된 프로 간 균형 조정 역할을 한다.

```java
Flux.range(0,10000)
	.parallel() //parallel연산자를 사용함으로써 ParallelFlux라는 다른 유형의 Flux를 동작
	.runOn(Schedulers.parallel()) //runOn 연산자를 적용해 PublishOn을 내부 Flux에 적용, 서로 다른 워커에서 처리 중인 데이터와 관련된 작업을 분배할 수 있다.
	.map()
	.filter()
	.subscribe()
```

- ParallelFlux는 다수의 Flux를 추상화한 것으로 Flux간에 데이터의 크기가 균형을 이룬다.

#### 4.  Scheduler

스케줄러는 Scheduler.schedule과 Scheduler.createWorker라는 두가지 핵심 메서드를 가진 인터페이스이다.

Schedlue메서드를 사용하면 Runnable 작업을 예약하는 것이 가능하다.

createWorker는 동일한 방법으로 Runnable 작업을 예약할 수 있는 Worker 인터페이스의 인스턴스를 제공한다.

Scheduler vs. worker

- Scheduler 인터페이스가 워커 풀을 나타냄
- Worker는 Thread 또는 리소스를 추상화한 것

기본적으로 리액터는 스케줄러 인터페이스에 대한 주요한 세가지 구현체를 제공

- SingleScheduler : 모든 작업을 한 개의 전용 워커에 예약. 시간에 의존적인 방식,주기적인 이벤트를 예약 가능
- ParallelScheduler : 고정된 크기의 작업자 풀에서 작동. 시간 관련 예약 이벤트를 처리
- ElasticScheduler : 동적으로 작업자를 만들고 스레드 풀을 캐시. I/O 작업에 적합



### 리액터 컨텍스트

Context는 스트림을 따라 전달되는 인터페이스

> Context 인터페이스의 핵심 아이디어는 나중에 런타임 단계에서 필요한 컨텍스트 정보에 액세스 할 수 있도록 하는 것

- Context는 Immutable 객체라서 새로운 요소를 추가하면 새로운 인스턴스로 변경됨
- 멀티스레딩 액세스 모델을 고려하여 설계됨
- 스트림에 컨텍스트를 제공할 수 있는 유일한 방법
- Context가 조립단계에서 제공되면 모둔 구족자는 동일한 정적 컨텍스트를 공유하게 되며, 이는 각 Subscriber가 별도의 Context를 가져야하는 경우에는 유용하지 않을 수 있다.
- 전체 생명 주기 중에서 각 Subscriber에게 별도의 컨텍스트가 제공될 수 있는 유일한 단계는 구독 단계이다.

##### CoreSubscriber

```java
public interface CoreSubscriber<T> extends Subscriber<T> {
	default Context currentContext(){// 현재 Context를 반환
		return Context.empty();
	}
}
```

현재 Context를 수정할 수 있는 유일한 연산자는 `subscriberContext`이다.

- 다운스트림의  Context와 전달된 매개변수를 병합하는 방식으로 CoreSubscriber를 구현한다.



### 프로젝트 리액터의 내부 구조

#### 매크로 퓨전

주로 조립 단계에서 발생

목적은 연산자를 다른 연산자로 교체하는 것 (조립된 흐름을 최적화하는 것)

- Mono는 하나 또는 0개의 원소만 처리하기  위해 고도로 최적화된 것을 이미 확인함

- 동시에 Flux 내부의 일부 연산자도 하나 또는 0개의 원소를 처리할수 있다.(just,empty, error)

- 대부분의 경우 이러한 간단한 연산자들은 다른 변환 작업과 함께 사용됨

- 결과적으로 이러한 오버헤드를 줄이는 것이 중요함

- 이를 위해 리액터는 조립 단계동안 최적화를 제공, 업스트림 publisher가 callable 또는 ScalarCallable과 같은 인터페이스를 구현한 경우에는 Publisher를 최적화된 연산자로 교체한다.

  ```java
  Flux.just(1)
  	.publishOn(...)
  	.map(...)
  //원소가 생성된 직후에 원소에 대한 실행을 다른 워커로 옮김
  ```

  - 최적화가 적용되지 않는 경우, 이런 작업 흐름은 다른 워커의 원소를 유지하기 위한 큐를 만들어 할당 

    ​	-> 큐에 원소를 입축력하기 위한 volatile 읽기 및 쓰기가 발생 

    ​		-> 단순한 Flux 타입 변환 작업이 지나치게 많이 실행됨

  - 한 원소의 공급이 ScalarCallable#call로 표현될 수 있기 때문에 publishOn 연산자를 추가 큐를 만들 필요가 없는 subscribeOn으로 치환할 수 있음

  - 이러한 최적화로 인해 다운스트림의 실행이 변경되지 않으므로 최적확된 스트림을 실행해 동일한 결과를 얻을 수 있다.

#### 마이크로 퓨전

좀 더 복잡한 최적화

런타임 최적화 및 공유 리소스 재사용과 관련이 있음

예시 : 조건부 연산자

```java
Flux.from(factory)
	.filter(inspectionDepartment)
	.subscribe(store)
```

다운스트림 가입자는 소스에 특정 수의 원소를 요청 -> 연산자 체인을 통해 원소를 내보내는 동안 각 원소는 조건부 연산자를 통해 이동하기 때문에 일부 원소는 거부될수 있음 -> 다운스트림 요구 사항을 충족시키려면 거부된 항목에 대해 연산자가 request(1) 메서드를 호출해 부족한 항목을 보충해야함 

현재 리액티브 라이브러리의  설계에 따라 request 메서드 실행은 추가적인 CPU 부하를 발생시킴

즉, 필터 연산자 같은 조건부 연산자가 전체 성능에 지대한 영향을 미칠 수 있다.

이러한 이유로 마이크로 퓨전 유형의 `ConditionalSubscriber`가 존재함

최적화를 통해 소스측에서 조건을 바로 확인하고 추가적인 request 메서드 호출 없이 필요한 개수를 전송 할 수 있음

```java
Flux.just(1,2,3)
	.publishOn(Schdulers.parallel())
	.concatMap(i -> Flux.range(0,1)
						.publishOn(Schedulers.parallel()))
    .subscribe();
```

