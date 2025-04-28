# 유전 알고리즘 기반 주문 최적화 시스템

이 프로젝트는 유전 알고리즘을 활용하여 주문 및 SKU(Stock Keeping Unit) 최적화 문제를 해결하는 시스템을 구현합니다. 유전 알고리즘의 교차(Crossover)와 돌연변이(Mutation) 연산을 통해 최소한의 다양한 SKU로 주문을 처리할 수 있는 최적의 조합을 찾습니다.

## 프로젝트 개요

- **언어**: Kotlin
- **프레임워크**: Spring Boot (GenerationProblemApplication)
- **알고리즘**: 유전 알고리즘(Genetic Algorithm)

## 문제 정의

다양한 SKU를 포함하는 여러 주문이 있을 때, 최소한의 다양한 SKU 종류로 주문을 처리할 수 있는 최적의 주문 조합을 찾는 것이 목표입니다. 이는: 

- 재고 관리 단순화
- 보관 비용 절감
- 주문 처리 효율성 증가
- 공급망 최적화

등의 비즈니스 이점을 제공합니다.

## 주요 구성 요소

### 데이터 모델

- **Order**: 주문 정보를 담는 클래스
  - 주문 ID
  - SKU 목록
  
- **OrderSku**: 주문 내 상품 정보를 담는 클래스
  - SKU ID
  - 수량
  
- **Individual**: 유전 알고리즘의 개체를 나타내는 클래스
  - 주문 목록
  - 적합도(fitness) 계산 기능

### 유전 알고리즘 구현

`GenerationAlgorithm` 클래스에 구현된 유전 알고리즘은 다음 기능을 제공합니다:

- **초기 개체군 생성**: 무작위 SKU와 주문으로 초기 개체군 생성
- **적합도 평가**: 개체의 적합도는 고유 SKU ID의 개수(적을수록 좋음)
- **선택**: 적합도가 높은(SKU 종류가 적은) 개체를 선호
- **교차(Crossover)**: 두 개체의 주문을 섞어 새로운 개체 생성
- **돌연변이(Mutation)**: 두 개체 간 주문을 교환하여 다양성 확보
- **세대 교체**: 엘리트 전략과 교차/돌연변이를 통한 새로운 세대 생성

## 알고리즘 설정

```kotlin
data class Config(
    val maxGenerationCount: Int = 100,   // 최대 세대 수
    val eliteRatio: Double = 0.3,        // 엘리트 비율
    val crossRatio: Double = 0.5,        // 교차 연산 확률
    val initSkuKindCount: Int = 1000,    // 초기 SKU 종류 수
    val initOrderCount: Int = 1000,      // 초기 주문 수
)
```

## 사용 방법

1. 프로젝트 클론
   ```
   git clone [repository-url]
   ```

2. Gradle로 빌드
   ```
   ./gradlew build
   ```

3. 애플리케이션 실행
   ```
   ./gradlew bootRun
   ```
   
   또는 Main.kt 직접 실행:
   ```
   ./gradlew run
   ```

## 알고리즘 실행 예시

```
Maximum individual: 42
[Generation 0]  Maximum individual: 39
[Generation 1]  Maximum individual: 36
[Generation 2]  Maximum individual: 32
...
[Generation 99]  Maximum individual: 15
```

## 향후 개선 사항

- 다양한 돌연변이 전략 구현
- 적합도 함수 개선 (SKU별 비용, 처리 시간 등 고려)
- 다양한 선택 방법 구현 (룰렛 휠, 토너먼트 등)
- 매개변수 튜닝 기능 추가
- 성능 측정 및 시각화 도구

## 라이센스

[라이센스 정보]

## 기여

버그 신고, 기능 제안, 풀 리퀘스트는 언제든지 환영합니다.
