import { useEffect, useState } from "react";
import "./ProfessorDashboard.css";

function ProfessorDashboard() {

    const [courses, setCourses] = useState([]);
    const [selectedCourse, setSelectedCourse] = useState(null);
    const [students, setStudents] = useState([]);
    const [records, setRecords] = useState([]);
    const [overall, setOverall] = useState([]);
    const [summary, setSummary] = useState(null);

    const [selectedDate, setSelectedDate] = useState(
        new Date().toISOString().split("T")[0]
    );

    const [activePage, setActivePage] = useState("dashboard");
    const [loading, setLoading] = useState(true);
    const [message, setMessage] = useState("");

    const professorId = localStorage.getItem("userId");
    const professorName = localStorage.getItem("name");
    const token = localStorage.getItem("token");

    const headers = {
        Authorization: `Bearer ${token}`
    };

    useEffect(() => {
        loadCourses();
    }, []);

    const loadCourses = async () => {

        try {

            const response = await fetch(
                `http://localhost:8080/courses/professor/${professorId}`,
                {
                    headers
                }
            );

            if (!response.ok) {
                throw new Error("Unable to load courses");
            }

            const data = await response.json();

            setCourses(data);

            if (data.length > 0) {
                selectCourse(data[0]);
            }

        } catch (error) {

            console.error(error);
            setMessage("Unable to load courses.");

        } finally {

            setLoading(false);

        }
    };

    const selectCourse = async (course) => {

        setSelectedCourse(course);
        setMessage("");

        try {

            await Promise.all([
                loadStudents(course.id),
                loadSummary(course.id, selectedDate),
                loadRecords(course.id, selectedDate),
                loadOverall(course.id)
            ]);

        } catch (error) {

            console.error(error);
            setMessage("Unable to load course data.");

        }
    };

    const loadStudents = async (courseId) => {

        const response = await fetch(
            `http://localhost:8080/courses/${courseId}/students`,
            {
                headers
            }
        );

        if (response.ok) {
            const data = await response.json();
            setStudents(data);
        }
    };

    const loadSummary = async (courseId, date) => {

        const response = await fetch(
            `http://localhost:8080/attendance/course/${courseId}/summary/${date}`,
            {
                headers
            }
        );

        if (response.ok) {
            const data = await response.json();
            setSummary(data);
        }
    };

    const loadRecords = async (courseId, date) => {

        const response = await fetch(
            `http://localhost:8080/attendance/course/${courseId}/records/${date}`,
            {
                headers
            }
        );

        if (response.ok) {
            const data = await response.json();
            setRecords(data);
        }
    };

    const loadOverall = async (courseId) => {

        const response = await fetch(
            `http://localhost:8080/attendance/course/${courseId}/overall`,
            {
                headers
            }
        );

        if (response.ok) {
            const data = await response.json();
            setOverall(data);
        }
    };

    const handleDateChange = async (event) => {

        const date = event.target.value;

        setSelectedDate(date);

        if (!selectedCourse) return;

        await Promise.all([
            loadSummary(selectedCourse.id, date),
            loadRecords(selectedCourse.id, date)
        ]);
    };

    const logout = () => {

        localStorage.clear();
        window.location.reload();

    };

    const totalStudents =
        summary?.totalStudents ?? students.length;

    const presentStudents =
        summary?.presentStudents ?? 0;

    const absentStudents =
        summary?.absentStudents ?? 0;

    const attendancePercentage =
        summary?.attendancePercentage ?? 0;

    if (loading) {

        return (
            <div className="prof-loading">
                <div className="prof-loader"></div>
                <p>Loading professor dashboard...</p>
            </div>
        );

    }

    return (

        <div className="prof-dashboard">

            {/* Sidebar */}

            <aside className="prof-sidebar">

                <div className="prof-brand">

                    <div className="prof-brand-icon">
                        📍
                    </div>

                    <div>
                        <h2>Attendance</h2>
                        <span>Professor Portal</span>
                    </div>

                </div>

                <nav className="prof-nav">

                    <button
                        className={
                            activePage === "dashboard"
                                ? "prof-nav-active"
                                : ""
                        }
                        onClick={() => setActivePage("dashboard")}
                    >
                        <span>▦</span>
                        Dashboard
                    </button>

                    <button
                        className={
                            activePage === "courses"
                                ? "prof-nav-active"
                                : ""
                        }
                        onClick={() => setActivePage("courses")}
                    >
                        <span>📚</span>
                        My Courses
                    </button>

                    <button
                        className={
                            activePage === "attendance"
                                ? "prof-nav-active"
                                : ""
                        }
                        onClick={() => setActivePage("attendance")}
                    >
                        <span>✓</span>
                        Attendance
                    </button>

                    <button
                        className={
                            activePage === "students"
                                ? "prof-nav-active"
                                : ""
                        }
                        onClick={() => setActivePage("students")}
                    >
                        <span>👥</span>
                        Students
                    </button>

                </nav>

                <div className="prof-sidebar-bottom">

                    <div className="prof-user">

                        <div className="prof-avatar">
                            {professorName?.charAt(0).toUpperCase()}
                        </div>

                        <div>
                            <strong>{professorName}</strong>
                            <small>Professor</small>
                        </div>

                    </div>

                    <button
                        className="prof-logout"
                        onClick={logout}
                    >
                        ↪ Logout
                    </button>

                </div>

            </aside>


            {/* Main */}

            <main className="prof-main">

                <header className="prof-topbar">

                    <div>

                        <p>Professor Portal</p>

                        <h1>
                            Welcome back, {professorName}
                        </h1>

                    </div>

                    <div className="prof-top-avatar">
                        {professorName?.charAt(0).toUpperCase()}
                    </div>

                </header>


                {message && (
                    <div className="prof-notification">
                        {message}
                    </div>
                )}


                {/* Dashboard */}

                {activePage === "dashboard" && (

                    <>

                        {/* Course selector */}

                        <section className="prof-course-selector">

                            <div>

                                <label>Select Course</label>

                                <select
                                    value={selectedCourse?.id || ""}
                                    onChange={(e) => {

                                        const course = courses.find(
                                            c => c.id === Number(e.target.value)
                                        );

                                        if (course) {
                                            selectCourse(course);
                                        }

                                    }}
                                >

                                    {courses.map(course => (

                                        <option
                                            key={course.id}
                                            value={course.id}
                                        >
                                            {course.name}
                                        </option>

                                    ))}

                                </select>

                            </div>

                            <div className="prof-date">

                                <label>Date</label>

                                <input
                                    type="date"
                                    value={selectedDate}
                                    onChange={handleDateChange}
                                />

                            </div>

                        </section>


                        {/* Stats */}

                        <section className="prof-stats">

                            <div className="prof-stat-card">

                                <div className="prof-stat-icon blue">
                                    👥
                                </div>

                                <div>
                                    <span>Total Students</span>
                                    <strong>{totalStudents}</strong>
                                </div>

                            </div>


                            <div className="prof-stat-card">

                                <div className="prof-stat-icon green">
                                    ✓
                                </div>

                                <div>
                                    <span>Present</span>
                                    <strong>{presentStudents}</strong>
                                </div>

                            </div>


                            <div className="prof-stat-card">

                                <div className="prof-stat-icon red">
                                    !
                                </div>

                                <div>
                                    <span>Absent</span>
                                    <strong>{absentStudents}</strong>
                                </div>

                            </div>


                            <div className="prof-stat-card">

                                <div className="prof-stat-icon purple">
                                    📊
                                </div>

                                <div>
                                    <span>Attendance</span>
                                    <strong>
                                        {Number(attendancePercentage).toFixed(1)}%
                                    </strong>
                                </div>

                            </div>

                        </section>


                        {/* Daily Attendance */}

                        <section className="prof-section">

                            <div className="prof-section-header">

                                <div>
                                    <h2>Daily Attendance</h2>
                                    <p>
                                        Attendance records for {selectedDate}
                                    </p>
                                </div>

                            </div>

                            <AttendanceRecords
                                records={records}
                            />

                        </section>

                    </>

                )}


                {/* Courses */}

                {activePage === "courses" && (

                    <section className="prof-section">

                        <div className="prof-section-header">

                            <div>
                                <h2>My Courses</h2>
                                <p>
                                    Courses assigned to you
                                </p>
                            </div>

                        </div>

                        <div className="prof-course-grid">

                            {courses.map(course => (

                                <div
                                    className={
                                        selectedCourse?.id === course.id
                                            ? "prof-course-card selected"
                                            : "prof-course-card"
                                    }
                                    key={course.id}
                                    onClick={() => selectCourse(course)}
                                >

                                    <div className="prof-course-icon">
                                        📖
                                    </div>

                                    <h3>{course.name}</h3>

                                    <p>
                                        Course ID: {course.id}
                                    </p>

                                    <div className="prof-course-footer">

                    <span>
                      👥 {course.students?.length || "View"} students
                    </span>

                                        <span>
                      →
                    </span>

                                    </div>

                                </div>

                            ))}

                        </div>

                    </section>

                )}


                {/* Attendance */}

                {activePage === "attendance" && (

                    <section className="prof-section">

                        <div className="prof-section-header">

                            <div>
                                <h2>Attendance</h2>
                                <p>
                                    View attendance for a selected date
                                </p>
                            </div>

                            <input
                                className="attendance-date"
                                type="date"
                                value={selectedDate}
                                onChange={handleDateChange}
                            />

                        </div>

                        <AttendanceRecords
                            records={records}
                        />

                    </section>

                )}


                {/* Students */}

                {activePage === "students" && (

                    <section className="prof-section">

                        <div className="prof-section-header">

                            <div>
                                <h2>Students</h2>
                                <p>
                                    Students enrolled in {selectedCourse?.name}
                                </p>
                            </div>

                        </div>

                        <StudentsTable
                            students={students}
                        />

                    </section>

                )}

            </main>

        </div>
    );
}


/* Attendance Records */

function AttendanceRecords({ records }) {

    if (!records || records.length === 0) {

        return (
            <div className="prof-empty">
                <div>📋</div>
                <p>No attendance records found for this date.</p>
            </div>
        );

    }

    return (

        <div className="prof-table-container">

            <table>

                <thead>

                <tr>
                    <th>Student</th>
                    <th>Roll Number</th>
                    <th>Status</th>
                </tr>

                </thead>

                <tbody>

                {records.map((record, index) => (

                    <tr key={record.studentId || index}>

                        <td>

                            <div className="student-cell">

                                <div className="student-avatar">
                                    {record.name?.charAt(0).toUpperCase()}
                                </div>

                                <span>{record.name}</span>

                            </div>

                        </td>

                        <td>
                            {record.rollNumber}
                        </td>

                        <td>

                <span
                    className={
                        record.present
                            ? "prof-status present"
                            : "prof-status absent"
                    }
                >
                  {record.present
                      ? "Present"
                      : "Absent"}
                </span>

                        </td>

                    </tr>

                ))}

                </tbody>

            </table>

        </div>

    );
}


/* Students Table */

function StudentsTable({ students }) {

    if (!students || students.length === 0) {

        return (
            <div className="prof-empty">
                <div>👥</div>
                <p>No students enrolled in this course.</p>
            </div>
        );

    }

    return (

        <div className="prof-table-container">

            <table>

                <thead>

                <tr>
                    <th>Student</th>
                    <th>Roll Number</th>
                    <th>Email</th>
                </tr>

                </thead>

                <tbody>

                {students.map(student => (

                    <tr key={student.id}>

                        <td>

                            <div className="student-cell">

                                <div className="student-avatar">
                                    {student.name?.charAt(0).toUpperCase()}
                                </div>

                                <span>{student.name}</span>

                            </div>

                        </td>

                        <td>
                            {student.rollNumber}
                        </td>

                        <td>
                            {student.email}
                        </td>

                    </tr>

                ))}

                </tbody>

            </table>

        </div>

    );
}

export default ProfessorDashboard;