function [SrcList,DstList] = GetSLXLines(sys)

    % load Simulink sys
    load_system(sys);
    % create sys strings
    slxname = [sys,'.slx'];
    xmlname = [sys,'_xml.xml'];
    % save the xml file
    save_system(slxname,xmlname,'ExportToXML', true)
    % get the lines of the file
    lines = GetFileLines(xmlname);
    % number of lines in the xml file
    NumXMLLines = length(lines);
    % strings
    LineStartStr1 = '<Line>';
    LineStartStr2 = '<Line LineType';
    LineEndStr = '</Line>';
%     BranchStartStr = '<Branch';
%     BranchEndStr = '</Branch>';
%     DstPortStr = 'DstPort';
%     SrcPortStr = 'SrcPort';
    DstBlockStr = 'DstBlock';
    SrcBlockStr = 'SrcBlock';
    StartTagStr = '<';
    EndTagStr = '>';

    % go through lines and find the start and end positions of the line tag
    for k = 1:NumXMLLines
        LineStartStrPos(k) = ~isempty([strfind(lines{k},LineStartStr1),...
            strfind(lines{k},LineStartStr2)]);
        LineEndStrPos(k) = ~isempty(strfind(lines{k},LineEndStr));
    end
    % get the lines numbers for the start and end line tags
    LineStartStrPos = find(LineStartStrPos);
    LineEndStrPos = find(LineEndStrPos);
    % number of lines in the model
    NumLines = length(LineStartStrPos);
    
    % go through each line tag
    for k = 1:NumLines
        % initialize stuff
        LinesStruct(k).SrcPort = ''; LinesStruct(k).DstPort = '';
        LinesStruct(k).SrcBlock = ''; LinesStruct(k).DstBlock = '';
        SrcStrInd = 0; DstStrInd = 0;
        % go through each line in a specific line tag
        for kk = LineStartStrPos(k):LineEndStrPos(k)
            % find source blocks
            if ~isempty(strfind(lines{kk},SrcBlockStr))
                SrcStrInd = SrcStrInd + 1;
                % get the block string
                BlockStr = GetBlockStr(sys,lines,StartTagStr,EndTagStr,kk);
                % get port string
                PortStr = GetPortStr(sys,lines,StartTagStr,EndTagStr,kk+1);
                % 
                if isempty(strfind(PortStr,'LConn')) && isempty(strfind(PortStr,'RConn'))
                    PortStr = ['outport/',PortStr];
                end
                % create full port string
                FullPortStr = [BlockStr,'/',PortStr];
                LinesStruct(k).SrcPort{SrcStrInd} = FullPortStr;
                LinesStruct(k).SrcBlock{SrcStrInd} = BlockStr;
            end
            % find destination blocks
            if ~isempty(strfind(lines{kk},DstBlockStr))
                DstStrInd = DstStrInd + 1;
                % get the block string
                BlockStr = GetBlockStr(sys,lines,StartTagStr,EndTagStr,kk);
                % get port string
                PortStr = GetPortStr(sys,lines,StartTagStr,EndTagStr,kk+1);
                % 
                if isempty(strfind(PortStr,'LConn')) && isempty(strfind(PortStr,'RConn'))
                    PortStr = ['inport/',PortStr];
                end
                % create full port string
                FullPortStr = [BlockStr,'/',PortStr];
                LinesStruct(k).DstPort{DstStrInd} = FullPortStr;
                LinesStruct(k).DstBlock{DstStrInd} = BlockStr;
            end
        end
    end

    % src and dst list
    SrcList = {''};     
    DstList = {''};
    ind = 0;
    for k = 1:NumLines
        for kk = 1:length(LinesStruct(k).SrcPort)
            for kkk = 1:length(LinesStruct(k).DstPort)
                ind = ind + 1;
                
                src = LinesStruct(k).SrcPort{kk};
                
                % modify the string for compatibility
                src = regexprep(src,'\s+',' '); % remove the double space if present
                src = strrep(src, ' ', '_'); % IDs in XMI cannot contain white-space characters
                src = strrep(src,sprintf('\n'),'_'); % IDs in XMI cannot contain newline characters
                
                SrcList{ind} = src;
                
                dst = LinesStruct(k).DstPort{kkk};
                
                % modify the string for compatibility
                dst = regexprep(dst,'\s+',' '); % remove the double space if present
                dst = strrep(dst, ' ', '_'); % IDs in XMI cannot contain white-space characters
                dst = strrep(dst,sprintf('\n'),'_'); % IDs in XMI cannot contain newline characters
                
                DstList{ind} = dst;
            end
        end
    end
    
end

% get the lines of the file
function lines = GetFileLines(xmlname)
    % open the file in read mode
    fileID = fopen(xmlname,'r');
    % read the file contents
    tline = fgetl(fileID);
    k = 0;
    while ischar(tline)
        k = k + 1;
        lines{k} = tline;
        tline = fgetl(fileID);
    end
    % close the file
    fclose(fileID);
end

% get the block string from xml file
function BlockStr = GetBlockStr(sys,lines,StartTagStr,EndTagStr,kk)
    StartTagPos = strfind(lines{kk},StartTagStr);
    StartTagPos = StartTagPos(2);
    EndTagPos = strfind(lines{kk},EndTagStr);
    EndTagPos = EndTagPos(1);
    BlockStr = lines{kk}(EndTagPos+1:StartTagPos-1);
    BlockStr = [sys,'/',BlockStr];
end

% get port string from xml file
function PortStr = GetPortStr(sys,lines,StartTagStr,EndTagStr,kk)
    StartTagPos = strfind(lines{kk},StartTagStr);
    StartTagPos = StartTagPos(2);
    EndTagPos = strfind(lines{kk},EndTagStr);
    EndTagPos = EndTagPos(1);
    PortStr = lines{kk}(EndTagPos+1:StartTagPos-1);
end