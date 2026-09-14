ALTER TABLE request
    ADD COLUMN license_id BIGINT NULL,
    ADD CONSTRAINT fk_request_license FOREIGN KEY (license_id) REFERENCES license (id);