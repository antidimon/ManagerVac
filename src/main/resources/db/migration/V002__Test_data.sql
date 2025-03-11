INSERT INTO users (username, email, password) VALUES
('Test1', 'Test1@test.com', '$2a$05$4XB2TWob6AORddJvOPjW0eDUKDKjOAyH1jI/VCyxaMkG9CTC67/nS'),
('Test2', 'Test2@test.com', '$2a$05$4XB2TWob6AORddJvOPjW0eDUKDKjOAyH1jI/VCyxaMkG9CTC67/nS'),
('Test3', 'Test3@test.com', '$2a$05$4XB2TWob6AORddJvOPjW0eDUKDKjOAyH1jI/VCyxaMkG9CTC67/nS');

INSERT INTO projects (project_name) VALUES
('Project 1'),
('Project 2'),
('Project 3');

INSERT INTO project_members (project_id, user_id, role) VALUES
(1, 1, 'ADMIN'),
(1, 2, 'MEMBER'),
(2, 2, 'ADMIN'),
(3, 3, 'ADMIN');

INSERT INTO tasks (project_id, task_name, description, status, priority, deadline) VALUES
(1, 'Design UI', 'Design the user interface', 'TODO', 'HIGH', '2025-03-18 00:00:00'),
(1, 'Implement Backend', 'Implement the backend API', 'INPROGRESS', 'MEDIUM', '2025-03-25 00:00:00'),
(2, 'Database Setup', 'Set up the database', 'COMPLETED', 'HIGH', '2025-03-10 00:00:00');

INSERT INTO task_developers (task_id, user_id) VALUES
(1, 1),
(2, 2),
(3, 2);


INSERT INTO comments (user_id, task_id, comment) VALUES
(1, 1, 'Initial design looks great!'),
(2, 2, 'Backend implementation is underway.'),
(3, 2, 'Database is set up and ready to go.');