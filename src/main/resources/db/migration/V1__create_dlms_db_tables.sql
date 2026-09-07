CREATE TABLE person (
                        id                BIGINT AUTO_INCREMENT PRIMARY KEY,
                        national_number   VARCHAR(20)  NOT NULL,
                        first_name        VARCHAR(100) NULL,
                        last_name         VARCHAR(100) NULL,
                        birth_day         DATE         NULL,
                        address           VARCHAR(255) NULL,
                        phone_number      VARCHAR(20)  NULL,
                        email             VARCHAR(255) NOT NULL,
                        nationality       VARCHAR(100) NULL,
                        photo             VARCHAR(500) NULL,
                        gender            VARCHAR(10)  NULL,

                        CONSTRAINT uq_person_national_number UNIQUE (national_number),
                        CONSTRAINT uq_person_email UNIQUE (email),
                        CONSTRAINT chk_person_gender CHECK (gender IN ('MALE', 'FEMALE'))
);
CREATE TABLE driver (
                        id             BIGINT AUTO_INCREMENT PRIMARY KEY,
                        driver_number  VARCHAR(20) NOT NULL,
                        creation_date  DATE        NULL,
                        person_id      BIGINT      NOT NULL,

                        CONSTRAINT uq_driver_driver_number UNIQUE (driver_number),
                        CONSTRAINT uq_driver_person_id UNIQUE (person_id),
                        CONSTRAINT fk_driver_person FOREIGN KEY (person_id) REFERENCES person (id)
);
CREATE TABLE app_user (
                      id             BIGINT AUTO_INCREMENT PRIMARY KEY,
                      email          VARCHAR(255) NOT NULL,
                      password       VARCHAR(255) NULL,
                      creation_date  DATE         NULL,
                      user_status    VARCHAR(20)  NULL,
                      role           VARCHAR(20)  NULL,
                      person_id      BIGINT       NOT NULL,

                      CONSTRAINT uq_user_email UNIQUE (email),
                      CONSTRAINT uq_user_person_id UNIQUE (person_id),
                      CONSTRAINT fk_user_person FOREIGN KEY (person_id) REFERENCES person (id),
                      CONSTRAINT chk_user_status CHECK (user_status IN ('ACTIVE', 'SUSPENDED')),
                      CONSTRAINT chk_user_role CHECK (role IN ('ADMIN', 'AGENT'))
);
CREATE TABLE license_category (
                                  id                          BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  name                        VARCHAR(100)  NOT NULL,
                                  description                 VARCHAR(255)  NULL,
                                  minimum_age                 INT           NOT NULL,
                                  validation_duration_years   INT           NOT NULL,
                                  fee                         DECIMAL(10,2) NOT NULL,

                                  CONSTRAINT uq_license_category_name UNIQUE (name)
);
CREATE TABLE exam_type_config (
                                  id         BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  exam_type  VARCHAR(20)   NOT NULL,
                                  fee        DECIMAL(10,2) NOT NULL,

                                  CONSTRAINT uq_exam_type_config_exam_type UNIQUE (exam_type),
                                  CONSTRAINT chk_exam_type_config_exam_type CHECK (exam_type IN ('VISION', 'THEORY', 'PRACTICAL'))
);
CREATE TABLE request (
                         id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
                         creation_date         DATE        NOT NULL,
                         cancellation_date     DATE        NULL,
                         request_status        VARCHAR(20) NOT NULL,
                         service_type          VARCHAR(30) NULL,
                         person_id             BIGINT      NOT NULL,
                         category_id           BIGINT      NULL,
                         cancelled_by_user_id  BIGINT      NULL,
                         original_request_id   BIGINT      NULL,

                         CONSTRAINT fk_request_person FOREIGN KEY (person_id) REFERENCES person (id),
                         CONSTRAINT fk_request_category FOREIGN KEY (category_id) REFERENCES license_category (id),
                         CONSTRAINT fk_request_cancelled_by FOREIGN KEY (cancelled_by_user_id) REFERENCES app_user (id),
                         CONSTRAINT fk_request_original_request FOREIGN KEY (original_request_id) REFERENCES request (id),
                         CONSTRAINT chk_request_status CHECK (request_status IN ('NEW', 'CANCELLED', 'COMPLETE')),
                         CONSTRAINT chk_request_service_type CHECK (service_type IN (
                                                                                     'NEW_LICENSE', 'EXAM_RETAKE', 'RENEWAL', 'LOST_DUPLICATE',
                                                                                     'DAMAGED_DUPLICATE', 'UNBLOCKING', 'INTERNATIONAL_LICENSE'
                             ))
);
CREATE TABLE license (
                         id                       BIGINT AUTO_INCREMENT PRIMARY KEY,
                         license_number           VARCHAR(30)   NOT NULL,
                         holder_photo             VARCHAR(500)  NULL,
                         issue_date               DATE          NOT NULL,
                         expiration_date          DATE          NOT NULL,
                         conditions                VARCHAR(500)  NULL,
                         holder_national_number   VARCHAR(20)   NOT NULL,
                         holder_full_name         VARCHAR(200)  NOT NULL,
                         holder_birth_date        DATE          NOT NULL,
                         issue_reason             VARCHAR(30)   NOT NULL,
                         blocking_status          VARCHAR(20)   NOT NULL,
                         driver_id                BIGINT        NOT NULL,
                         category_id              BIGINT        NOT NULL,
                         issuing_agent_id         BIGINT        NOT NULL,

                         CONSTRAINT uq_license_license_number UNIQUE (license_number),
                         CONSTRAINT fk_license_driver FOREIGN KEY (driver_id) REFERENCES driver (id),
                         CONSTRAINT fk_license_category FOREIGN KEY (category_id) REFERENCES license_category (id),
                         CONSTRAINT fk_license_issuing_agent FOREIGN KEY (issuing_agent_id) REFERENCES app_user (id),
                         CONSTRAINT chk_license_issue_reason CHECK (issue_reason IN (
                                                                                     'NEW', 'RENEWAL', 'REPLACEMENT_LOST', 'REPLACEMENT_DAMAGED'
                             )),
                         CONSTRAINT chk_license_blocking_status CHECK (blocking_status IN ('BLOCKED', 'UNBLOCKED'))
);
CREATE TABLE license_block (
                               id               BIGINT AUTO_INCREMENT PRIMARY KEY,
                               reason           VARCHAR(255)   NOT NULL,
                               fine_amount      DECIMAL(10,2)  NOT NULL,
                               blocking_date    DATE           NOT NULL,
                               unblocking_date  DATE           NULL,
                               license_id       BIGINT         NOT NULL,

                               CONSTRAINT fk_license_block_license FOREIGN KEY (license_id) REFERENCES license (id)
);
CREATE TABLE international_license (
                                       id                            BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       issue_date                    DATE        NOT NULL,
                                       expiration_date               DATE        NOT NULL,
                                       international_permit_status   VARCHAR(20) NOT NULL,
                                       license_id                    BIGINT      NOT NULL,
                                       driver_id                     BIGINT      NOT NULL,

                                       CONSTRAINT fk_intl_license_license FOREIGN KEY (license_id) REFERENCES license (id),
                                       CONSTRAINT fk_intl_license_driver FOREIGN KEY (driver_id) REFERENCES driver (id),
                                       CONSTRAINT chk_intl_license_status CHECK (international_permit_status IN ('ACTIVE', 'CANCELLED'))
);
CREATE TABLE exam (
                      id                BIGINT AUTO_INCREMENT PRIMARY KEY,
                      exam_type         VARCHAR(31) NOT NULL,
                      result            VARCHAR(20) NULL,
                      appointment_date  DATE        NOT NULL,
                      result_date       DATE        NULL,
                      request_id        BIGINT      NOT NULL,
                      score             INT         NULL,

                      CONSTRAINT fk_exam_request FOREIGN KEY (request_id) REFERENCES request (id),
                      CONSTRAINT chk_exam_type CHECK (exam_type IN ('VISION', 'THEORY', 'PRACTICAL')),
                      CONSTRAINT chk_exam_result CHECK (result IN ('PASSED', 'FAILED')),
                      CONSTRAINT chk_exam_score CHECK (score IS NULL OR (score BETWEEN 0 AND 40))
);
CREATE TABLE payment (
                         id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
                         amount             DECIMAL(10,2) NOT NULL,
                         payment_date       DATE          NOT NULL,
                         payment_type       VARCHAR(30)   NOT NULL,
                         request_id         BIGINT        NOT NULL,
                         exam_id            BIGINT        NULL,
                         license_block_id   BIGINT        NULL,

                         CONSTRAINT fk_payment_request FOREIGN KEY (request_id) REFERENCES request (id),
                         CONSTRAINT fk_payment_exam FOREIGN KEY (exam_id) REFERENCES exam (id),
                         CONSTRAINT fk_payment_license_block FOREIGN KEY (license_block_id) REFERENCES license_block (id),
                         CONSTRAINT chk_payment_type CHECK (payment_type IN (
                                                                             'THEORY_EXAM', 'PRACTICAL_EXAM', 'VISION_EXAM', 'SERVICE', 'APPLICATION_FEE', 'FINE'
                             ))
);