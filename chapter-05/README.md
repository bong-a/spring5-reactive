# Chapter 5. 스프링 부트 2와 함께하는 리액티브

리액터 프로젝트는 스프링 프레임워크 없이도 잘 작동하지만, 복잡한 기능을 제공하기 위한 응용 프로그램을 작성하기에는 부족하다.

## 스프링 부트가 해결한 문제와 해결방법

### 스프링 프레임워크

- 많은 혜택과 장점이 있음

- 하지만 제대로 사용하기 위해 깊은 이해가 필요함

  - 제어의 역전(IoC)가 그 예시가 될 수 있음

  순수 스프링 프레임워크에서 스프링 컨텍스트에서 빈 등록하기 위해 3가지이상의 방법이 있음

  - 이는 빈을 유연하게 구성하게 해줌
  - 하지만 XML 설정은 쉽게 디버깅할 수 없음(추가 도구 없이 정확성 검증이 어려움)
  - 코딩스타일 부재 및 개발 규칙 정용의 어려움이 있음
    - Bean 정의에 대한 접근 방식이 적절하지 않으면 팀의 한 개발자가 XML에 빈 설정하고 동시에 다른 개발자가 properties 파일통해 설정을 변경이 가능함
    - 따라서 프로젝트 초기 적응 기간이 필요 이상으로 길어짐

- 스프링 웹 모듈이나 스프링 데이터 모듈 등 복잡한 기능 제공

  - 두 모듈 실행하기 까지 많은 설정이 필요
  - 응용 프로그램이 플랫폼 독립적이어야할 때 문제가 발생함 -> 비즈니스 관련 코드에 비해 설정 및 상용구 코드가 많은 비중을 차지하기 때문

### Spring Roo를 사용해 애플리케이션 개발 속도 향상

> Spring Roo의 핵심 아이디어는 설정보다 관습(convention-over-configuration)이다.

- 인프라 및 도메인 모델을 초기화
-  몇가지 명령으로 REST API를 작성할 수 있는 커맨드라인 사용자 인터페이스 제공
- 개발 프로세스를 단순화함

#### 단점

- 대규모 응용프로그램 개발 프로젝트에는 적용하기 어려움
- 프로젝트 구조가 복잡해지거나 사용된 기술이 스프링 프레임워크의 범위를 벗어나면 문제가 발생
- 일반적인 용도로 인기가 너무 없었음 

따라서 결국 신속한 응용 프로그램 개발에 대한 고민은 해결되지 않음



## 스프링 부트의 필수 요소

### 빠르게 성장하는 애플리케이션에 대한  핵심 요소로서의 스프링부트

> 2012년 말,마이크 영스트롬이 스프링 아키텍처 전체 변경 및 단순화하여 비즈니스 로직을 빨리 구축할 수 있도록 제안
>
> -> 제안은 거부됐지만 스프링 프레임워크 사용을 극적으로 단순화하는 새로운 프로젝트를 만들도록 동기를 부여
>
> -> 2013년 중반 스프링 부트 프로젝트 출시

- 스프링 부트 핵심 아이디어는 애플리케이션 개발 프로세스를 단순화하고 사용자가 추가적인 인프라 설정 없이 새 프로젝트를 시작할 수 있도록 하는 것

- 컨테이너가 없이 실행되는 웹 애플리케이션 아이디어와 실행 가능한 fat JAR 기술 도입

  ```java
  @SpringBootApplication
  public class MyApplication{
  	public static void main(String[] args){
  		SpringApplication.run(MyApplication.class,args);
  	}
  }
  ```

  - IoC 컨테이너 실행하는 데 필요한 `@SpringBootApplication`이라는 애노테이션이 매우 중요함

- 스프링 부트는 그래들이나 메이븐과 같은 빌드 툴과 모듈들의 묶음이다.

- 일반적으로 스프링부트는 두개의 핵심모듈에 의존함

  1. `spring-boot` 모듈 : spring-IoC 컨테이너와 관련된 모든 가능한 기본 구성을 함께 제공하는 모듈
  2. `spring-boot-autoconfigure` 모듈 : 스프링 데이터, 스프링 MVC, 스프링 웹플럭스 등과 같은 기존 스프링 프로젝트에서 필요한 모든 설정을 제공
     - 특정 읜존성이 추가될 때까지 모든 설정은 비활성화되어 있다.

- 스프링부트는 일반적으로 `starter`라는 단어가 들어있는 모듈에 대한 새로운 개념을 정의함

  - 자바 코드는 포함하지 않지만,`spring-boot-configure`에 의해 모든 관련 의존성을 가져와 설정을 활성화함

- Spring Roo보다 훨씬 유연함

참고 : [스프링부트 2.0 - 마이크로서비스와 리액티브 프로그래밍](https://www.aladin.co.kr/shop/wproduct.aspx?ItemId=179597489)



## 스프링 부트 2.0 및 스프링 프레임워크의 반응성

### 스프링 부트 2.0에서의 리액티브

스프링 MVC와 스프링 데이터 모듈의 블로킹 특성으로 인해 프로그래밍 패러다임을 리액티브로 전환하는 것만으로는 별다름 이점이 없었음

그래서 스프링 팀은 모듈 내부의 전체 패러다임을 변경하기로 함

따라서 스프링 생태계는 다수의 리액티브 모듈을 제공한다.

#### 스프링 코어 패키지에서의 리액티브

> 스프링 코어는 스프링 생태계의 핵심 모듈

스프링  프레임워크 5.X에서 눈에 띄는 개선사항은 RxJava 1/2 및 리액터 프로젝트 3과 같은 리액티브 스트림 및 리액티브 라이브러리에 대한 기본 지원이다.

##### 리액티브 타입으로 형변환 지원

- 리액티브 스트림 스펙 지원하기 위해 ReactiveAdapter 및 ReactiveAdapterRegistry 도입

- ReactiveAdapter : 임의의 타입을 Publisher<T>로 변화하거나, Publisher<T>를 Object로 변환해줌

  - 예제 : [MaybeReactiveAdapter.java](src/main/java/org/rpis5/chapters/chapter_05/core/MaybeReactiveAdapter.java)

- ReactiveAdapterRegistry  : ReactiveAdapter 인스턴스를 한 곳에서 유지하고 액세스를 일반활 할 수 있음

  - 다양한 리액티브 타입에 대한 ReactiveAdapter 인스턴스의 풀을 의미함

  ```java
  ReactiveAdapterRegistry
      .getSharedInstance() // 싱글턴 인스턴스 제공
     	.registerReactiveType( // 매개변수를 이용해 어댑터 등록할 수 있음
      	ReactiveTypeDescriptor.singleOptionalValue(Maybe.class, Maybe::empty),
      	rawMaybe -> ((Maybe<?>)rawMaybe).toFlowable(),
      	publisher -> Flowable.fromPublisher(publisher).singleElement()
      );
  //...
  ReactiveAdapter maybeAdapter = ReactiveAdapterRegistry
      .getSharedInstance() 
      .getAdapter(Maybe.class);	// 변환 수행해야하는 자바 클래스를 매개변수로 해서 어댑터 얻을 수 있음
  ```

##### 리액티브 I/O

리액티브 지원과 관련된 또 다른 기획적인 개선 사항은 Core I/O 패키지 보강이다.

- 스프링 코어 모듈은 byte 인스턴스의 버퍼를 추상화한 DataBuffer라는 클래스를 도입함
  - 추상화를 통해 별도의 형 변환 없이도 다양한 바이트 버퍼를 지원

- PooledDataBuffer라는 하위 인터페이스는 레퍼런스 카운팅을 활성화하고 효율적인 메모리 관리를 가능하게 함

- 스프링 코어 버전 5에는 I/O 작업을 리액티브 스트림 형태로 할 수 있게 해주는 DataBufferUtils 클래스가 있음

- 리액티브 코덱 : DataBuffer 인스턴스의 스트림을 객체의 스트림으로 변환해 돌려주는 작업을 간편하게 할 수 있음

  - 이를 통해 논블로킹 방식으로 직렬화된 데이터를 자바 객체로 또는 그 반대로 변환 할 수있음

  - 리액티브 스트림의 이점을 그대로 활용 가능

    - 전체 데이터 세트의 디코딩을 시작하기 위해 마지막 바이트를 모둔 수신할 때까지 기다릴 필요가 없음

      -> 전체 처리 시간을 줄일 수 있음

    - 객체의 전체 목록을 다 받기 전에도 인코딩을 시작하고 I/O 채널로 보낼수 있음

      -> 따라서 양방향 처리 속도를 모듀 향상시킬 수 있음

##### 웹에서의 리액티브

스프링 부트2에는 웹플럭스라는 새로운 웹 스타터 그룹이 포함돼 있다.

- 스타터 그룹은 높은 처리량과 빠른 반응속도를 필요로 하는 애플리케이션 개발에 새로운 가능성을 제공한다.
- 리액티브 스트림 어댑터 위에 구축되며, 일반적인 서블릿 API 3.1 기반 서버를 지원함과 동시에 네티 및 언더토와 같은 서버 엔진과도 통합된다.
- 이런 방식으로 웹플럭스는 논블로킹 방식의 기반을 제공
- 비즈니스 로직과 서버 엔진간의 통합을 위한 핵심 추상화를 제공해 리액티브 스트림의 새로운 가능성을 열었다고 할 수 있다.
- 리액터3를 광범위하게 사용
- 내장된 배압 지원 기능 제공 -> I/O 안정성을 보장
- 클라이언트 측면에서 논블로킹 통신을 지원하는 새로운 WebClient 클래스를 제공

##### 스프링 데이터에서의 리액티브

스프링 데이터 프레임워크 5에서 데이터베이스 레이어에 대한 리액티브 및 논블로킹 액세스를 할 수있는 새로운 가능성을 보여줌

리액터 프로젝트의 리액티브 타입을 이용해 리액터 워크플로와 원할한 통합을 가능하게 하는 ReactiveCrudRepository 인터페이스를 제공함

-> 결과적으로 데이터베이스 커넥터를 완벽하게 리액티브 응용 프로그램과 통합 할 수 있음

스프링 데이터에서 리액티브 통합이 가능한 스토리지 메서드 목록

- 스프링 데이터 Mongo 리액티브 모듈
- 스프링 데이터 Cassandra 리액티브 모듈
- 스프링 데이터 Redis 리액티브 모듈
- 스프링 데이터 Couchbase 리액티브 모듈

머지않아 리액티브 JDBC 커넥션도 제공될 것이다.



##### 스프링 세션에서의 리액티브

- 웹플럭스 모듈이 세션을 효율적으로 관리할 수 있는 추상화를 제공
  - 스프링 세션은 Mono 타입으로 저장된 세션에 대한 비동기 논블로킹 액세스를 허용하는 ReactiveSessionRepository를 도입
- 스프링 세션은 리액티브 스프링 데이터를 통한 세션 저장소로 Redis와 리액티브 통합을 제공



##### 스프링 시큐리티에서의 리액티브

과거의 스프링 시큐리니는 SecurityContext 인스턴스의 저장 방법으로 ThreadLocal을 사용함

- 한 스레드 내에서 실행될 때는 잘 작동함
- 비동기 통신이 발생할 때는 문제 발생
  - ThreadLocal 내용을 다른 스레드로 전송하기 위한 추가적인 공수를 투입해야함
  - Thread 인스턴스 사이에 전환이 발생한느 곳마다 이러한 추가 작업을 해줘야 함
  - 스프링 프레임워크는 ThreadLocal  확장을 사용해 스레드간의 SecurityContext 전달을 단순화하지만, 리액터 프로젝트 또는 다른 리액티브 라이브러리에서 리액티브 프로그래밍 패러다임을 적용할 때는 여전히 문제가 발생할 수 있다.

차세대 스프링 시큐리티는 Flux 또는 Mono 스트림 내에서 SecurityContext를 전송하기 위해 리액터의 컨텍스트를 사용함



##### 스프링 클라우드에서의 리액티브

스프링 클라우드 생태계는 스프링 프레임워크를 사용해 리액티브 시스템을 구죽하는 것을 목표로 삼았다.

-> 리액티브 프로그래밍 패러다임 영향을 받음

-> 측히 게이트웨이라는 분산 시스템 진입점에 영향을 미침

- 동기식 블로킹 방식으로 요청을 라우팅하는 넷플릭스의 Zuul 프로젝트

- 스프링 클라우드는 

  - 스프링 웹플럭스 위에 구축된 새로운 스프링 클라우드 게이트웨이 모듈을 도입
  - 리액터 프로젝트 3을 지원

  -> 비동기 논블로킹 라우팅을 지원

스프링 클라우드 스트림은 리액터 프로젝트의 지원을 얻었으며, 보다 세분화된 스트리밍 모델을 도입함

스프링 클라우드 펑션 도입 -> Faas 솔루션으로서 자체 기능을 구축하는 데 필요한 필수 구성 요소를 제공

추가적인 인프라 없이 일반적인 개발환경에서는 스프링 클라우드 펑션 모듈을 사용할 수 없음

-> 스프링 클라우드 데이터 플로를 활용하면 가능



##### 스프링 테스트에서의 리액티브

- 웹플럭스 기반 웹 애플리케이션을 테스트 할 수 있는 WebTestClient 제공

- 애노테이션을 사용해 테스트 스위트에 대한 자동 구성을 처리함
- Publisher를 테스트하기 위해 리액터 테스트 모듈 제공



##### 리액티브 모니터링하기

리액터 프로젝트와 리액티브 스프링 프레임워크를 기반으로 제작된 리액티브 시스템의 운영 환경은 모든 중용한 운영 지표를 보여줄 수 있어야 함

이를 위해 애플리케이션 모니터링을 위한 여러 가지 세분화된 옵션을 제공함

- 리액티브 스트림 내에서 여러 이벤트를 추적할 수 있는 Flux#matrics 메서드
- 차게대 스프링 액추에이터는 웹플럭스와 완벽하게 통합되며 지표를 위한 엔드포인트를 효율적으로 노출하기 위해 비동기 논블로킹 프로그래밍 모델을 사용함
- 스프링 클라우드 슬루스(Sleuth) : 즉시 사용 가능한 분산 추적을 제공
  - 리액터 프로젝트로 리액티브 프로그래밍을 지원 -> 응용 프로그램 내의 모든 리액티브 워크플로를 추적할 수 있음


