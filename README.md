# 📝 To-Do List Application (Spring Boot)

Simple task management application supporting 2 parts:
- Web interface with Thymeleaf
- REST API system secured by session (`JSESSIONID`)

---

## 🚀 Main features

### ✅ Web
- Register, login user
- Add / edit / delete task
- Update status: todo → in progress → done
- Redirect permissions (`USER`, `ADMIN`)
- Custom 403 page when lack of permission

### ✅ REST API
- `POST /api/register`: Register account
- `POST /api/login`: Login, return `JSESSIONID`
- `POST /api/logout`: Log out
- `GET /api/user-info`: Return current user information at
- `GET /api/users`: (only `ADMIN`) Get list of users
- `GET /api/tasks`: Get list of tasks
- `POST /api/tasks/create`: Create task
- `PATCH /api/tasks/{id}/update`: Update task
- `DELETE /api/tasks/{id}/delete`: Delete task
- `PATCH /api/tasks/{id}/toggle-status`: Toggle task status
- `DELETE /api/tasks/{id}/update`: Update task

---

## 🛡️ Security

- Use Spring Security (form login + session)
- API access permissions only allow `ADMIN` and `USER`
- Custom `AuthenticationEntryPoint`: API does not login → return 401 JSON
- Custom `AccessDeniedHandler`: API lacks permission → returns 403 JSON
- Only create `JSESSIONID` after successful login

---

## ⚙️ Install & run

```bash
# 1. Clone project

# 2. Open with IntelliJ / VSCode / Eclipse

# 3. Run project (Spring Boot main class)