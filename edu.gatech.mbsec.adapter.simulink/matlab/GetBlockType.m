% given a block handle, get the library block name with compatibiilty
% considerations
function blockType = GetBlockType(blockhandle)

    % try to get the reference block
    ReferenceBlock = get(blockhandle,'ReferenceBlock');
    
    % if it is empty, it must be a built-in block
    if isempty(ReferenceBlock)
        blockType = ['built_in/',get(blockhandle,'BlockType')];
    else
        blockType = ReferenceBlock;
    end
    
    % modify the string for compatibility
    blockType = regexprep(blockType,'/','_'); % replace forward slashes
    blockType = regexprep(blockType,'\s+',' '); % remove the double space if present
    blockType = regexprep(blockType,' ','_'); % replace single spaces

end

% http://www.mathworks.com/matlabcentral/answers/44069