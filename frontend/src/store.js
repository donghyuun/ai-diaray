// store.js for zustand

import {create} from 'zustand';
import {persist} from "zustand/middleware";

const useStore = create(
    persist(
        (set, get) => ({
            isLogined: false,
            setIsLogined: (value) => set({isLogined: value}),
            username: "",
            setUsername: (value) => set({username: value}),
            userId: -1,
            setUserId: (value) => set({userId: value}),
            role: "",
            setRole: (value) => set({role: value})
        }),
        {
            name: 'login state and username and userId',
        }
    )
)

export default useStore