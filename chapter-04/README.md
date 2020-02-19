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

## 리액터 프로젝트의 고급 기능

## 리액터 프로젝트의 가장 중요한 구현 세부 정보

## 가장 자주 사용되는 리액티브 타입의 비교

## 리액터 라이브러리로 구현한 비즈니스 사례

