import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import './index.css';

import Story from './Story';
import Start from './Start';
import Signup from './Signup';
import ScenarioList from './ScenarioList';
import RecordList from './RecordList';
import RecordDetail from "./RecordDetail";
import User from './User';
import Rank from "./Rank";
import reportWebVitals from './reportWebVitals';

function renderApp() {
    const root = ReactDOM.createRoot(document.getElementById('root'));
    root.render(
        <React.StrictMode>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<Start />} />
                    <Route path="/signup" element={<Signup />} />
                    <Route path="/scenarios/:scenarioId/scenes/:sceneId" element={<Story />} />
                    <Route path="/scenarios" element={<ScenarioList />} />
                    <Route path="/records" element={<RecordList />} />
                    <Route path="/recordDetail" element={<RecordDetail />} />
                    <Route path="/user/my" element={<User />} />
                    <Route path="/rankings" element={<Rank />} />
                </Routes>
            </BrowserRouter>
        </React.StrictMode>
    );
}

renderApp();

