import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Login from "../pages/Login.js";
import Register from "../pages/Register.js";
import Home from "../pages/Home.js";
import BodyClassManager from "./BodyClassManager.js";
import Profile from "../pages/Profile.js";
const AppRoutes = () => {
  return (
    <Router>
      <BodyClassManager />
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/" element={<Home />} />
        <Route path="/profile/:accountID" element={<Profile />} />
      </Routes>
    </Router>
  );
};

export default AppRoutes;
