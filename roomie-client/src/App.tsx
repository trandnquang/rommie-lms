import { Routes, Route, Navigate } from "react-router-dom";
import { RoomListPage } from "./features/rooms/pages/RoomListPage";

function App() {
    return (
        <Routes>
            <Route path="/" element={<Navigate to="/rooms" replace />} />
            <Route path="/rooms" element={<RoomListPage />} />
            <Route path="*" element={<Navigate to="/rooms" replace />} />
        </Routes>
    );
}

export default App;
