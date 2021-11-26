import React, { useState } from "react";
import { useThemeSwitcher } from "react-css-theme-switcher";
import { Switch} from "antd";



export default function Theme() {
  const [isDarkMode, setIsDarkMode] =useState();

  const { switcher, status, themes } = useThemeSwitcher();

  const toggleTheme = (isChecked) => {
    setIsDarkMode(isChecked);
    switcher({ theme: isChecked ? themes.dark : themes.light });
  };

  // Avoid theme change flicker
  if (status === "loading") {
    return null;
  }

  return (
      <div>
           <Switch  checkedChildren="🌞" unCheckedChildren="🌒 " checked={isDarkMode} onChange={toggleTheme} />
      </div>
     
    
  );
}
