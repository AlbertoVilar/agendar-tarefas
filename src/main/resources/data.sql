-- Seed somente de roles (usuários serão criados via API /auth/register)
INSERT INTO roles (nome_role)
SELECT 'ROLE_USER' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE nome_role = 'ROLE_USER');

INSERT INTO roles (nome_role)
SELECT 'ROLE_ADMIN' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE nome_role = 'ROLE_ADMIN');