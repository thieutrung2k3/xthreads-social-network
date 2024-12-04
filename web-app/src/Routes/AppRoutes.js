import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Login from "../Components/pages/Login.js";
import Register from "../Components/pages/Register.js";
import Home from "../Components/pages/Home.js";

const AppRoutes = () => {
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/" element={<Home />} />
      </Routes>
    </Router>
  );
};

export default AppRoutes;
