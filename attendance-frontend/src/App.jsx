import { useState } from "react";
import StudentDashboard from "./StudentDashboard";
import "./App.css";
import ProfessorDashboard from "./ProfessorDashboard.jsx";

function App() {

  const existingToken = localStorage.getItem("token");
  const existingRole = localStorage.getItem("role");

  if (existingToken && existingRole === "STUDENT") {
    return <StudentDashboard />;
  }
  if (existingToken && existingRole === "PROFESSOR") {
    return <ProfessorDashboard />;
  }

  const [role, setRole] = useState("student");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  const handleLogin = async (e) => {
    e.preventDefault();
    setMessage("");

    const endpoint =
        role === "student"
            ? "http://localhost:8080/students/login"
            : "http://localhost:8080/professors/login";

    try {
      const response = await fetch(endpoint, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          email,
          password
        })
      });

      const data = await response.json();

      if (!response.ok) {
        setMessage("Invalid email or password");
        return;
      }

      localStorage.setItem("token", data.token);
      localStorage.setItem("role", data.role);
      localStorage.setItem("userId", data.id);
      localStorage.setItem("name", data.name);

      window.location.reload();

    } catch (error) {
      console.error(error);
      setMessage("Unable to connect to the server");
    }
  };

  return (
      <div className="login-page">

        <div className="login-card">

          <div className="login-header">
            <div className="logo">📍</div>

            <h1>Attendance System</h1>

            <p>
              Smart attendance with secure GPS verification
            </p>
          </div>

          <div className="role-selector">

            <button
                className={role === "student" ? "active" : ""}
                onClick={() => setRole("student")}
                type="button"
            >
              🎓 Student
            </button>

            <button
                className={role === "professor" ? "active" : ""}
                onClick={() => setRole("professor")}
                type="button"
            >
              👨‍🏫 Professor
            </button>

          </div>

          <form onSubmit={handleLogin}>

            <label>Email</label>

            <input
                type="email"
                placeholder="Enter your email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
            />

            <label>Password</label>

            <input
                type="password"
                placeholder="Enter your password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
            />

            <button className="login-button" type="submit">
              Login
            </button>

          </form>

          {message && (
              <p className="login-message">
                {message}
              </p>
          )}

        </div>

      </div>
  );
}

export default App;