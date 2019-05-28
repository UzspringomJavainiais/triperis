INSERT INTO role (name, date_created, description) VALUES('ROLE_USER', CURRENT_TIMESTAMP, 'The basic user');
INSERT INTO role (name, date_created, description) VALUES('ROLE_ADMIN', CURRENT_TIMESTAMP, 'The basic admin');

INSERT INTO status_code (name, description) VALUES('PLANNING', 'Used when trip is in planning phase');
INSERT INTO status_code (name, description) VALUES('ONGOING', 'Used when trip is in progress');
INSERT INTO status_code (name, description) VALUES('CANCELED', 'Used when trip was canceled');
INSERT INTO status_code (name, description) VALUES('FINISHED', 'The basic admin');