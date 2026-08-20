import { useEffect, useState } from "react";
import "./StudentDashboard.css";

function StudentDashboard() {

    const [courses, setCourses] = useState([]);
    const [attendance, setAttendance] = useState([]);
    const [activePage, setActivePage] = useState("dashboard");
    const [loading, setLoading] = useState(true);
    const [message, setMessage] = useState("");

    const name = localStorage.getItem("name");
    const studentId = localStorage.getItem("userId");
    const token = localStorage.getItem("token");

    useEffect(() => {
        loadData();
    }, []);

    const loadData = async () => {

        try {

            const courseResponse = await fetch(
                `http://localhost:8080/students/${studentId}/courses`,
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );

            const attendanceResponse = await fetch(
                `http://localhost:8080/attendance/student/${studentId}`,
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );

            if (courseResponse.ok) {
                const courseData = await courseResponse.json();
                setCourses(courseData);
            }

            if (attendanceResponse.ok) {
                const attendanceData = await attendanceResponse.json();
                setAttendance(attendanceData);
            }

        } catch (error) {

            console.error(error);
            setMessage("Unable to load dashboard data.");

        } finally {

            setLoading(false);

        }
    };

    const markAttendance = async (courseId) => {

        setMessage("");

        if (!navigator.geolocation) {
            setMessage("Geolocation is not supported by your browser.");
            return;
        }

        setMessage("Getting your location...");

        navigator.geolocation.getCurrentPosition(
            async (position) => {

                const latitude = position.coords.latitude;
                const longitude = position.coords.longitude;

                try {

                    const response = await fetch(
                        `http://localhost:8080/attendance/student/${studentId}/course/${courseId}`,
                        {
                            method: "POST",
                            headers: {
                                "Content-Type": "application/json",
                                Authorization: `Bearer ${token}`
                            },
                            body: JSON.stringify({
                                latitude,
                                longitude
                            })
                        }
                    );

                    const data = await response.json();

                    if (!response.ok) {
                        setMessage(
                            data.message || "Unable to mark attendance."
                        );
                        return;
                    }

                    setMessage("✓ Attendance marked successfully!");

                    loadData();

                } catch (error) {

                    console.error(error);
                    setMessage("Unable to connect to the server.");

                }
            },

            () => {
                setMessage(
                    "Location permission is required to mark attendance."
                );
            }
        );
    };

    const logout = () => {

        localStorage.clear();
        window.location.reload();

    };

    const totalClasses = attendance.length;

    const presentClasses = attendance.filter(
        (record) => record.present
    ).length;

    const attendancePercentage =
        totalClasses === 0
            ? 0
            : Math.round((presentClasses / totalClasses) * 100);

    if (loading) {
        return (
            <div className="loading-screen">
                <div className="loader"></div>
                <p>Loading dashboard...</p>
            </div>
        );
    }

    return (
        <div className="dashboard">

            {/* Sidebar */}

            <aside className="sidebar">

                <div className="brand">

                    <div className="brand-icon">
                        📍
                    </div>

                    <div>
                        <h2>Attendance</h2>
                        <span>System</span>
                    </div>

                </div>

                <nav>

                    <button
                        className={activePage === "dashboard" ? "nav-active" : ""}
                        onClick={() => setActivePage("dashboard")}
                    >
                        <span>▦</span>
                        Dashboard
                    </button>

                    <button
                        className={activePage === "courses" ? "nav-active" : ""}
                        onClick={() => setActivePage("courses")}
                    >
                        <span>▤</span>
                        My Courses
                    </button>

                    <button
                        className={activePage === "attendance" ? "nav-active" : ""}
                        onClick={() => setActivePage("attendance")}
                    >
                        <span>✓</span>
                        Attendance
                    </button>

                </nav>

                <div className="sidebar-bottom">

                    <div className="user-mini">

                        <div className="avatar">
                            {name?.charAt(0).toUpperCase()}
                        </div>

                        <div>
                            <strong>{name}</strong>
                            <small>Student</small>
                        </div>

                    </div>

                    <button className="logout-button" onClick={logout}>
                        ↪ Logout
                    </button>

                </div>

            </aside>

            {/* Main Content */}

            <main className="main-content">

                <header className="topbar">

                    <div>

                        <p className="welcome-small">
                            Student Portal
                        </p>

                        <h1>
                            Welcome back, {name}
                        </h1>

                    </div>

                    <div className="profile">

                        <div className="avatar large">
                            {name?.charAt(0).toUpperCase()}
                        </div>

                    </div>

                </header>

                {message && (
                    <div className="notification">
                        {message}
                    </div>
                )}

                {/* Dashboard */}

                {activePage === "dashboard" && (

                    <>

                        <section className="stats">

                            <div className="stat-card">

                                <div className="stat-icon blue">
                                    📚
                                </div>

                                <div>
                                    <span>My Courses</span>
                                    <strong>{courses.length}</strong>
                                </div>

                            </div>

                            <div className="stat-card">

                                <div className="stat-icon green">
                                    ✓
                                </div>

                                <div>
                                    <span>Present Classes</span>
                                    <strong>{presentClasses}</strong>
                                </div>

                            </div>

                            <div className="stat-card">

                                <div className="stat-icon purple">
                                    📊
                                </div>

                                <div>
                                    <span>Attendance</span>
                                    <strong>{attendancePercentage}%</strong>
                                </div>

                            </div>

                        </section>

                        <section className="section">

                            <div className="section-header">

                                <div>
                                    <h2>My Courses</h2>
                                    <p>Your currently enrolled courses</p>
                                </div>

                                <button
                                    className="view-button"
                                    onClick={() => setActivePage("courses")}
                                >
                                    View all →
                                </button>

                            </div>

                            <div className="course-grid">

                                {courses.length === 0 ? (

                                    <div className="empty">
                                        <div>📚</div>
                                        <p>You are not enrolled in any courses.</p>
                                    </div>

                                ) : (

                                    courses.slice(0, 3).map((course) => (

                                        <div className="course-card" key={course.id}>

                                            <div className="course-top">

                                                <div className="course-icon">
                                                    📖
                                                </div>

                                                <span className="course-status">
                          Active
                        </span>

                                            </div>

                                            <h3>{course.name}</h3>

                                            <p>
                                                👨‍🏫{" "}
                                                {course.professor?.name || "Professor not assigned"}
                                            </p>

                                            <button
                                                className="attendance-button"
                                                onClick={() => markAttendance(course.id)}
                                            >
                                                📍 Mark Attendance
                                            </button>

                                        </div>

                                    ))

                                )}

                            </div>

                        </section>

                        <section className="section">

                            <div className="section-header">

                                <div>
                                    <h2>Recent Attendance</h2>
                                    <p>Your latest attendance records</p>
                                </div>

                                <button
                                    className="view-button"
                                    onClick={() => setActivePage("attendance")}
                                >
                                    View all →
                                </button>

                            </div>

                            <AttendanceTable
                                attendance={attendance.slice(-5).reverse()}
                            />

                        </section>

                    </>

                )}

                {/* Courses Page */}

                {activePage === "courses" && (

                    <section className="section page-section">

                        <div className="section-header">

                            <div>
                                <h2>My Courses</h2>
                                <p>All courses you are enrolled in</p>
                            </div>

                        </div>

                        <div className="course-grid">

                            {courses.map((course) => (

                                <div className="course-card" key={course.id}>

                                    <div className="course-top">

                                        <div className="course-icon">
                                            📖
                                        </div>

                                        <span className="course-status">
                      Active
                    </span>

                                    </div>

                                    <h3>{course.name}</h3>

                                    <p>
                                        👨‍🏫{" "}
                                        {course.professor?.name || "Professor not assigned"}
                                    </p>

                                    <button
                                        className="attendance-button"
                                        onClick={() => markAttendance(course.id)}
                                    >
                                        📍 Mark Attendance
                                    </button>

                                </div>

                            ))}

                        </div>

                    </section>

                )}

                {/* Attendance Page */}

                {activePage === "attendance" && (

                    <section className="section page-section">

                        <div className="section-header">

                            <div>
                                <h2>Attendance History</h2>
                                <p>Your complete attendance records</p>
                            </div>

                            <div className="attendance-percentage">
                                {attendancePercentage}%
                                <span>overall</span>
                            </div>

                        </div>

                        <AttendanceTable attendance={attendance} />

                    </section>

                )}

            </main>

        </div>
    );
}


/* Attendance Table */

function AttendanceTable({ attendance }) {

    if (attendance.length === 0) {

        return (
            <div className="empty">
                <div>📋</div>
                <p>No attendance records found.</p>
            </div>
        );

    }

    return (

        <div className="table-container">

            <table>

                <thead>

                <tr>
                    <th>Course</th>
                    <th>Date</th>
                    <th>Time</th>
                    <th>Status</th>
                </tr>

                </thead>

                <tbody>

                {attendance.map((record, index) => (

                    <tr key={record.id || index}>

                        <td>
                            {record.course?.name || "Course"}
                        </td>

                        <td>
                            {record.date}
                        </td>

                        <td>
                            {record.time}
                        </td>

                        <td>

                <span
                    className={
                        record.present
                            ? "status present"
                            : "status absent"
                    }
                >
                  {record.present ? "Present" : "Absent"}
                </span>

                        </td>

                    </tr>

                ))}

                </tbody>

            </table>

        </div>

    );
}

export default StudentDashboard;