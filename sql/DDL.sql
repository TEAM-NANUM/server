-- 데이터베이스가 존재하면 삭제
DROP DATABASE IF EXISTS nanum_db;

-- 새로운 데이터베이스 생성
CREATE DATABASE nanum_db;

-- nanum_db 데이터베이스 선택
USE nanum_db;

-- 사용자 그룹 테이블
CREATE TABLE IF NOT EXISTS user_group(
    user_group_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    point         BIGINT
);

-- 토큰 블랙리스트 테이블
CREATE TABLE token_blacklist (
                                 token VARCHAR(512) NOT NULL,
                                 PRIMARY KEY (token)
);



-- 케러셀 테이블
CREATE TABLE IF NOT EXISTS carousel (
    carousel_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    img_url VARCHAR(255)
 );

-- 사용자 테이블
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    user_role ENUM('guest', 'host'),
    invite_code VARCHAR(255),
    create_at DATETIME,
    user_group_id BIGINT,
    uid BIGINT NOT NULL,
    FOREIGN KEY (user_group_id) REFERENCES user_group(user_group_id)
    );

-- 판매자 테이블
CREATE TABLE IF NOT EXISTS seller (
    seller_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    phone_number VARCHAR(20),
    email VARCHAR(255),
    password VARCHAR(255),
    zip_code VARCHAR(10), -- 우편번호
    default_address VARCHAR(255), -- 주소(일반주소)
    detail_address VARCHAR(255), -- 상세주소
    point BIGINT,
    create_at DATETIME
);

-- 1차 카테고리 테이블
CREATE TABLE IF NOT EXISTS category (
    category_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255)
);

-- 2차 카테고리 테이블
CREATE TABLE IF NOT EXISTS sub_category (
    sub_category_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    category_id BIGINT,
    FOREIGN KEY (category_id) REFERENCES category(category_id)
);


-- 상품 정보 테이블
CREATE TABLE IF NOT EXISTS product (
    product_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    price INT,
    unit INT,
    img_url VARCHAR(255),
    description TEXT,
    delivery_type ENUM('DIRECT', 'PARCEL'),
    rating_avg FLOAT,
    create_at DATETIME,
    seller_id BIGINT,
    sub_category_id BIGINT,
    carousel_id BIGINT,
    view_cnt BIGINT,
    review_cnt BIGINT,
    purchase_cnt BIGINT,
    FOREIGN KEY (seller_id) REFERENCES seller(seller_id),
    FOREIGN KEY (sub_category_id) REFERENCES sub_category(sub_category_id),
    FOREIGN KEY (carousel_id) REFERENCES carousel(carousel_id)
);

-- 주문 테이블
CREATE TABLE IF NOT EXISTS orders (
    order_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_cnt INT,
    delivery_status ENUM('PAYMENT_COMPLETE', 'DELIVERING', 'DELIVERED'),
    total_amount INT,
    product_id BIGINT,
    delivery_address VARCHAR(255),
    user_id BIGINT,
    create_at DATETIME,
    FOREIGN KEY (product_id) REFERENCES product(product_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);


-- 리뷰 테이블
CREATE TABLE IF NOT EXISTS review (
    review_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    rating FLOAT,
    comment VARCHAR(255),
    order_id BIGINT,
    create_at DATETIME,
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);

-- 배송 테이블
CREATE TABLE IF NOT EXISTS delivery (
    delivery_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nickname VARCHAR(255), -- 주소 별칭
    phone_number VARCHAR(20), -- 전화 번호
    zip_code VARCHAR(10),
    default_address VARCHAR(255),
    detail_address VARCHAR(255),
    is_default BOOLEAN,
    create_at DATETIME,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- 장바구니 테이블
CREATE TABLE IF NOT EXISTS cart (
    cart_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_cnt INT,
    product_id BIGINT,
    user_id BIGINT,
    FOREIGN KEY (product_id) REFERENCES product(product_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);