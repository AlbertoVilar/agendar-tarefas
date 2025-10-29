-- data.sql: seed de usuário usando função Java BCryptUtil via H2 ALIAS
CREATE ALIAS IF NOT EXISTS BCRYPT FOR "com.vilardev.Daily.util.BCryptUtil.encode";

INSERT INTO usuario (nome, email, senha)
SELECT 'Alberto Vilar', 'albertovilar1@gmail.com', BCRYPT('132747') WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE email = 'albertovilar1@gmail.com');

-- Seed de roles
INSERT INTO roles (nome_role)
SELECT 'ROLE_USER' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE nome_role = 'ROLE_USER');

INSERT INTO roles (nome_role)
SELECT 'ROLE_ADMIN' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE nome_role = 'ROLE_ADMIN');

-- Associação do usuário padrão à ROLE_USER
INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id
FROM usuario u
JOIN roles r ON r.nome_role = 'ROLE_USER'
WHERE u.email = 'albertovilar1@gmail.com'
AND NOT EXISTS (
    SELECT 1 FROM users_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id
);

-- Associação opcional do usuário padrão à ROLE_ADMIN (para testes de endpoint admin)
INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id
FROM usuario u
JOIN roles r ON r.nome_role = 'ROLE_ADMIN'
WHERE u.email = 'albertovilar1@gmail.com'
AND NOT EXISTS (
    SELECT 1 FROM users_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id
);