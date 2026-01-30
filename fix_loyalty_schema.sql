CREATE TABLE IF NOT EXISTS QuyDinhHoiVien (
    config_key VARCHAR(50) PRIMARY KEY,
    config_value VARCHAR(255)
);

INSERT IGNORE INTO QuyDinhHoiVien (config_key, config_value) VALUES ('POINTS_PER_10K', '1');
INSERT IGNORE INTO QuyDinhHoiVien (config_key, config_value) VALUES ('POINT_VALUE_VND', '1000');
