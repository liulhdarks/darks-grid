/**
 * 
 * Copyright 2015 The Darks Grid Project (Liu lihua)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package darks.grid.config;

import java.util.HashMap;
import java.util.Map;

public class ConstantConfig {

    Map<String, String> constantMap = new HashMap<String, String>();
    
    public ConstantConfig()
    {
        
    }
    
    public void setConstant(String key, String value)
    {
        constantMap.put(key, value);
    }
    
    public String getConstant(String key)
    {
        return constantMap.get(key);
    }

    public Map<String, String> getConstantMap() {
        return constantMap;
    }

    public void setConstantMap(Map<String, String> constantMap) {
        this.constantMap = constantMap;
    }
    
    
}
