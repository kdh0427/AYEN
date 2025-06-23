import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import './index.css';

import Story from './Story';
import Start from './Start';
import Signup from './Signup';
import ScenarioList from './ScenarioList';
import AchievementList from './AchievementList';
import User from './User';
import Rank from "./Rank";
import reportWebVitals from './reportWebVitals';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <React.StrictMode>
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Start />} />
                <Route path="/signup" element={<Signup />} />
                <Route path="/Story" element={<Story />} />
                <Route path="/scenarios" element={<ScenarioList />} />
                <Route path="/achievements" element={<AchievementList />} />
                <Route path="/user" element={<User />} />
                <Route path="/rank" element={<Rank />} />
            </Routes>
        </BrowserRouter>
    </React.StrictMode>
);

reportWebVitals();
