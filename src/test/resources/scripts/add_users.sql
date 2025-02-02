INSERT INTO users (user_username, user_password, user_role)
VALUES (
           'theotime',
           '$2a$12$kJI0zfOfvFMcEV2yRQS25e.2dVk6Lc00W8gmEdymvFGl2NkWoXOTi', -- 12 rounds bcrypt of 'donnenfeld'
           'ADMIN'
       ),
       (
           'user1',
           '$2a$12$gky1QtS4VZy6WjPJgy5zB.LvxaSg3TTdx4BwKdfCwrxeg6DTQJB.W', -- 12 rounds bcrypt of 'passwd1'
           'USER'
       );