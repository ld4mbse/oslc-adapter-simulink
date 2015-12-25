%{ 
 ********************************************************************************************
 Copyright (c) 2014 Model-Based Systems Engineering Center, Georgia Institute of Technology.

 All rights reserved. This program and the accompanying materials
 are made available under the terms of the Eclipse Public License v1.0
 and Eclipse Distribution License v. 1.0 which accompanies this distribution.
 
 The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 and the Eclipse Distribution License is available at
 http://www.eclipse.org/org/documents/edl-v10.php.
   
 Contributors:
   
 Axel Reichwein (axel.reichwein@koneksys.com)		- initial implementation       
 ********************************************************************************************
%}

function simulink2xmi(simulinkFolderPath)

try
% open folder with Simulink models
cd(simulinkFolderPath);

% helper functions can be accessed via the SimulinkXMIUtil struct
util = SimulinkXMIUtil;

% load all simulink models in workspace (old way)
% simulinkModelList = ls('*.slx');
% [modelCount, modelNameLength] = size(simulinkModelList);
% for i = 1 : modelCount    
%     % load Simulink model
%     load_system(strtrim(simulinkModelList(i,:)));      
% end


% support for loading Simulink files in subdirs
struct = rdir('**\*.slx');
cell = struct2cell(struct);
[rows, columns] = size(cell);


% create XMI file
simulinkXMIFile = fopen('simulinkWorkDir.xmi', 'w');
fprintf(simulinkXMIFile, '%s', '<?xml version="1.0" encoding="UTF-8"?>');
fprintf(simulinkXMIFile, '\n');
fprintf(simulinkXMIFile, '%s', '<mathworks.simulink:WorkingDirectory xmi:version="2.0" xmlns:xmi="http://www.omg.org/XMI" xmlns:mathworks.simulink="http://www.mathworks.com/products/simulink/">');
fprintf(simulinkXMIFile, '\n');

%**************************
% print all Simulink models
for i = 1 : columns 
   
    % get directory name
    file = strtrim(cell{1,i});
    fileParts = strsplit(file,'\');
    dirName = fileParts{1,1};
    
    % load Simulink model
    load_system(strtrim(cell{1,i})); 
    
    % only works if simulink models are already loaded in memory
    blockDiagrams_cell = find_system('type', 'block_diagram');
    
    model_cell = blockDiagrams_cell(1);
    
    % do not consider the simulink default model
    if  not(strcmp(blockDiagrams_cell(1),'simulink'))
        model_cell = blockDiagrams_cell(1);    
       
        % get the model name
        modelName = model_cell{1};
        
        % combined name
        combinedName2 = strcat(dirName,'---');
        combinedName = strcat(combinedName2,modelName);

%         % print image of simulink model
%         open_system(modelName);
%         outputfilename = strcat('../src/main/webapp/images/', modelName,'.jpg');
%         orient(modelName, 'portrait')
%         print -s -dbitmap outputfilename;
%         printoption = strcat('-s', modelName);
%         print(printoption, '-djpeg', outputfilename)
        
        
        % print model
        fprintf(simulinkXMIFile, '\t');
        fprintf(simulinkXMIFile, '%s', '<model name="');
       
        % print model name
        fprintf(simulinkXMIFile, combinedName);  
        fprintf(simulinkXMIFile, '%s', '">');
        fprintf(simulinkXMIFile, '\n');
         
        %*****************
        % print all blocks
        blocks = find_system(model_cell, 'Type', 'Block');
        for j = 1 : length(blocks) 
            block = blocks{j};                   
            blockhandle = get_param(block,'Handle');
                      
            % print block            
            fprintf(simulinkXMIFile, '\t\t');
            fprintf(simulinkXMIFile, '%s', '<block name="');
            
            % block parent
            blockParent = get(blockhandle, 'Parent');
            
            % print block name
            blockNameOriginal = get(blockhandle,'Name'); 
            blockName = regexprep(blockNameOriginal,'/','//');   % If the block name includes a slash character (/), you repeat the slash when you specify the block name in order to avoid later "Invalid Simulink object name" error
            fprintf(simulinkXMIFile, util.getBlockID(blockhandle));
            fprintf(simulinkXMIFile, '%s', '" type="');
          
            % print block type
            blockType = get(blockhandle,'BlockType'); 
            fprintf(simulinkXMIFile, blockType); 
            fprintf(simulinkXMIFile, '%s', '">');
            fprintf(simulinkXMIFile, '\n');
            
            % print block parameters
            
%             For character array inputs, strcat removes trailing ASCII white-space
%     characters: space, tab, vertical tab, newline, carriage return, and
%     form-feed. To preserve trailing spaces when concatenating character
%     arrays, use horizontal array concatenation, [s1, s2, ..., sN]
%             blockKey = strcat(blockParent, '/', blockName);            
            blockKey = horzcat(blockParent, '/', blockName);
            
%             % only consider referenced workspace variables
%             % this only works if all simulink models can be
%             % compiled/executed without error
%             refWkVariables = Simulink.findVars(blockKey);
%             for y = 1 : length(refWkVariables)
%                 try
%                 refWkVariable = refWkVariables(y);
%                 block_parameter_name = refWkVariable.Name;
%                 block_parameter_value = num2str(evalin('base', block_parameter_name));
%                 fprintf(simulinkXMIFile, '\t\t\t');
%                 fprintf(simulinkXMIFile, '%s', '<parameter name="');
%                 blockParQualifiedName = strcat(util.getBlockID(blockhandle),'/',block_parameter_name);
%                 fprintf(simulinkXMIFile, blockParQualifiedName);
%                 fprintf(simulinkXMIFile, '%s', '" value="');
%                 fprintf(simulinkXMIFile, block_parameter_value);
%                 fprintf(simulinkXMIFile, '%s', '"/>');
%                 fprintf(simulinkXMIFile, '\n'); 
%                 catch
%                     % referenced variable does not exist 
%                 end
%             end

            % code to consider all block parameters
            block_parameters = get_param(blockKey,'DialogParameters');           
            if ~(isempty(block_parameters) | isa(block_parameters, 'double'))               
            	block_parameter_names = fieldnames(block_parameters);
                for m = 1 : length(block_parameter_names)
                    block_parameter_name = block_parameter_names{m};   % {} returns string from cell array, () returns cell                
                    block_parameter_value = get_param(blockhandle,block_parameter_name);
                    if(~ischar(block_parameter_value))
                        if(~iscell(block_parameter_value))
                            continue;
                        end
                        newblockparvalue = '[';
                        for x = 1 : length(block_parameter_value)
                            value_segment = block_parameter_value{x};
                            if(~ischar(value_segment))
                               value_segment = value_segment{1};                         
                            end
                            value_segment = strrep(value_segment,',','');
                            if(x == 1)
                                newblockparvalue = strcat(newblockparvalue, value_segment);
                            else
                                newblockparvalue = strcat(newblockparvalue, ', ', value_segment);    
                            end  
                        end
                        newblockparvalue = strcat(newblockparvalue, ']');
                        block_parameter_value = newblockparvalue;
                    end
                    fprintf(simulinkXMIFile, '\t\t\t');
                    fprintf(simulinkXMIFile, '%s', '<parameter name="');
                    blockParQualifiedName = strcat(util.getBlockID(blockhandle),'/',block_parameter_name);
                    fprintf(simulinkXMIFile, blockParQualifiedName);
                    fprintf(simulinkXMIFile, '%s', '" value="');
%                     fprintf(simulinkXMIFile, block_parameter_value);
                    if(strcmp(block_parameter_value,'<'))
                        fprintf(simulinkXMIFile, '%s', '&lt;');
                    elseif(strcmp(block_parameter_value,'>'))
                        fprintf(simulinkXMIFile, '%s', '&gt;');
                    else
                        fprintf(simulinkXMIFile, block_parameter_value);
                    end
                    fprintf(simulinkXMIFile, '%s', '"/>');
                    fprintf(simulinkXMIFile, '\n'); 
                end

            end
            
            
            %******************
            % print input ports 
            blockPortHandles = get(blockhandle,'PortHandles'); 
            inputPortsHandle = blockPortHandles.Inport;
            if(length(inputPortsHandle) > 0)
                for k = 1 : length(inputPortsHandle)
                    inputPortHandle = inputPortsHandle(k);
                    fprintf(simulinkXMIFile, '\t\t\t');
                    fprintf(simulinkXMIFile, '%s', '<inputPort xmi:id="');                    
                    inputPortID = util.getPortID(inputPortHandle, blockhandle, 'inport', k);
                    % IDs in XMI cannot contain white-space characters
                    new_inputPortID2 = strrep(inputPortID, ' ', '_');
                    % IDs in XMI cannot contain newline characters
            		new_inputPortID = strrep(new_inputPortID2,sprintf('\n'),'_');
                    fprintf(simulinkXMIFile, new_inputPortID);
                    fprintf(simulinkXMIFile, '%s', '" id="');
                    fprintf(simulinkXMIFile, new_inputPortID);
                    fprintf(simulinkXMIFile, '%s', '"/>');
                    fprintf(simulinkXMIFile, '\n');
                end
            end
            
            %*******************
            % print output ports           
            outputPortsHandle = blockPortHandles.Outport;
            if(length(outputPortsHandle) > 0)
                for k = 1 : length(outputPortsHandle)
                    outputPortHandle = outputPortsHandle(k);
                    fprintf(simulinkXMIFile, '\t\t\t');
                    fprintf(simulinkXMIFile, '%s', '<outputPort xmi:id="');                    
                    outputPortID = util.getPortID(outputPortHandle, blockhandle, 'outport', k);
                    % IDs in XMI cannot contain white-space characters
                    new_outputPortID2 = strrep(outputPortID, ' ', '_');
                    % IDs in XMI cannot contain newline characters
            		new_outputPortID = strrep(new_outputPortID2,sprintf('\n'),'_');
                    fprintf(simulinkXMIFile, new_outputPortID);
                    fprintf(simulinkXMIFile, '%s', '" id="');
                    fprintf(simulinkXMIFile, new_outputPortID);
                    fprintf(simulinkXMIFile, '%s', '"/>');
                    fprintf(simulinkXMIFile, '\n');                  
                end
            end 
            fprintf(simulinkXMIFile, '\t\t');
            fprintf(simulinkXMIFile, '%s', '</block>');
            fprintf(simulinkXMIFile, '\n');                      
        end
        
        %****************
        % parse all lines
        lines = find_system(model_cell, 'FindAll', 'on', 'type', 'line');
        for m = 1 : length(lines)
            line = lines(m);                   
            lineHandle = get_param(line,'Handle');       
            srcPortHandle =  get(lineHandle, 'SrcPortHandle');
            srcBlockHandle =  get(lineHandle, 'SrcBlockHandle');            
            dstPortHandle =  get(lineHandle, 'DstPortHandle');
            dstBlockHandle =  get(lineHandle, 'DstBlockHandle');

            % print line                        
            fprintf(simulinkXMIFile, '\t\t');
            fprintf(simulinkXMIFile, '%s', '<line sourcePort="');        
            srcPortID = util.getPortID(srcPortHandle, srcBlockHandle, 'outport'); 
            % IDs in XMI cannot contain white-space characters
            new_srcPortID2 = strrep(srcPortID, ' ', '_');
            % IDs in XMI cannot contain newline characters
            new_srcPortID = strrep(new_srcPortID2,sprintf('\n'),'_');
            fprintf(simulinkXMIFile, '%s', new_srcPortID);
            for n = 1 : length(dstBlockHandle)
                if(n == 1)
                    fprintf(simulinkXMIFile, '%s', '" targetPort="');
                else
                    fprintf(simulinkXMIFile, '%s', ' ');
                end              
                dstPortID = util.getPortID(dstPortHandle, dstBlockHandle, 'inport', n);
                % IDs in XMI cannot contain white-space characters
                new_dstPortID2 = strrep(dstPortID, ' ', '_');
                % IDs in XMI cannot contain newline characters
            	new_dstPortID = strrep(new_dstPortID2,sprintf('\n'),'_');
                fprintf(simulinkXMIFile, '%s', new_dstPortID);
            end
            fprintf(simulinkXMIFile, '%s', '"/>');
            fprintf(simulinkXMIFile, '\n');         
        end
        
        fprintf(simulinkXMIFile, '\t');
        fprintf(simulinkXMIFile, '%s', '</model>');        
        fprintf(simulinkXMIFile, '\n');                
    end
%     save_system;
    close_system;
end

fprintf(simulinkXMIFile, '%s', '</mathworks.simulink:WorkingDirectory>');
fclose(simulinkXMIFile);

catch 
   
end

end


function util = SimulinkXMIUtil()
util.getPortID = @getPortID;
util.getBlockID = @getBlockID;
util.getBlockName = @getBlockName;
end

function portID = getPortID(portHandle, owningBlockHandle, portType, index)
% owning block name
owningBlockName = get(owningBlockHandle, 'Name');
owningBlockName = regexprep(owningBlockName,'\s',''); % block names contain white spaces (not suitable for port id)
% owning block parent name
owningBlockParent = get(owningBlockHandle, 'Parent');
% port can be identified by number or name
% port number
portNumbers = get(portHandle, 'PortNumber');
if(~iscell(portNumbers))
    portNumber = portNumbers;
else
    portNumber = portNumbers{index};
end
% port name
portNames = get(portHandle,'Name');
if(~iscell(portNames))
    portName = portNames;
else
    portName = portNames{index};
end
portName = strrep(portName,'<',''); %remove character < and > from port name
portName = strrep(portName,'>','');
% get string if owningBlockParent and owningBlockName are cell arrays
if(iscell(owningBlockParent))
   owningBlockParentStr = owningBlockParent{index};
   owningBlockNameStr = owningBlockName{index};
else
    owningBlockParentStr = owningBlockParent;
   owningBlockNameStr = owningBlockName;
end
% port ID
if(~strcmp(portName, ''))
    portID = strcat(owningBlockParentStr, '/', owningBlockNameStr, '/', portType, '/', portName);
else
    portID = strcat(owningBlockParentStr, '/', owningBlockNameStr, '/', portType, '/', num2str(portNumber));
end
end

function blockID = getBlockID(blockHandle)
% owning block name
blockName = get(blockHandle, 'Name');
blockName = strrep(blockName,' ',''); % block names contain white spaces (not suitable for port id)
blockName_parts = regexp(blockName, '[\f\n\r]', 'split'); % block names contain line breaks (not suitable for port id)
if(numel(blockName_parts) > 1)
    blockName = '';
    for z = 1 : length(blockName_parts)
        blockname_part = blockName_parts{z};
        blockName = strcat(blockName, blockname_part);
    end
end

% owning block parent name
blockParent = get(blockHandle, 'Parent');
blockID = strcat(blockParent, '/', blockName);
end