$(document).ready(function() {
	pageEditorViewModel.editorCommands([ {
		'name' : 'bold',
		'icon' : 'glyphicon glyphicon-bold',
		'text' : '',
		'exec' : function() {
			document.execCommand('bold', false, null);
		}
	}, {
		'name' : 'link',
		'icon' : 'glyphicon glyphicon-link',
		'text' : '',
		'exec' : function() {
			var href = prompt('Url for link', '');
			document.execCommand('createLink', false, href);
		}
	}, {
		'name' : 'unlink',
		'icon' : '',
		'text' : 'unlink',
		'exec' : function() {

		}
	}, {
		'name' : 'fontSize',
		'icon' : 'glyphicon glyphicon-text-height',
		'text' : '',
		'exec' : function() {
			// 1 to 7
			var size = prompt('text size (1-7)', '');
			document.execCommand('fontSize', false, size);
		}
	}, {
		'name' : 'foreColor',
		'icon' : 'glyphicon glyphicon-tint',
		'text' : '',
		'exec' : function() {
			var color = prompt('color', '');
			document.execCommand('foreColor', false, color);
		}
	}, {
		'name' : 'h1',
		'icon' : 'glyphicon glyphicon-header',
		'text' : '1',
		'exec' : function() {
			document.execCommand('formatBlock', false, '<h1>');
		}
	}, {
		'name' : 'h2',
		'icon' : 'glyphicon glyphicon-header',
		'text' : '2',
		'exec' : function() {
			document.execCommand('formatBlock', false, '<h2>');
		}
	}, {
		'name' : 'h3',
		'icon' : 'glyphicon glyphicon-header',
		'text' : '3',
		'exec' : function() {
			document.execCommand('formatBlock', false, '<h3>');
		}
	}, {
		'name' : 'h4',
		'icon' : 'glyphicon glyphicon-header',
		'text' : '4',
		'exec' : function() {
			document.execCommand('formatBlock', false, '<h4>');
		}
	}, {
		'name' : 'h5',
		'icon' : 'glyphicon glyphicon-header',
		'text' : '5',
		'exec' : function() {
			document.execCommand('formatBlock', false, '<h5>');
		}
	}, {
		'name' : 'h6',
		'icon' : 'glyphicon glyphicon-header',
		'text' : '6',
		'exec' : function() {
			document.execCommand('formatBlock', false, '<h6>');
		}
	}, {
		'name' : 'indent',
		'icon' : 'glyphicon glyphicon-indent-left',
		'text' : '',
		'exec' : function() {
			document.execCommand('indent', false, null);
		}
	}, {
		'name' : 'insertHorizontalRule',
		'icon' : '',
		'text' : 'hr',
		'exec' : function() {
			document.execCommand('insertHorizontalRule', false, null);
		}
	}, {
		'name' : 'insertImage',
		'icon' : 'glyphicon glyphicon-picture',
		'text' : '',
		'exec' : function() {
			var src = prompt('location of image', '');
			document.execCommand(this.name, false, src);
		}
	}, {
		'name' : 'insertOrderedList',
		'icon' : '',
		'text' : 'ol',
		'exec' : function() {
			document.execCommand(this.name, false, null);
		}
	}, {
		'name' : 'insertUnorderedList',
		'icon' : '',
		'text' : 'ul',
		'exec' : function() {
			document.execCommand(this.name, false, null);
		}
	}, {
		'name' : 'italic',
		'icon' : 'glyphicon glyphicon-italic',
		'text' : '',
		'exec' : function() {
			document.execCommand(this.name, false, null);
		}
	}, {
		'name' : 'justifyCenter',
		'icon' : 'glyphicon glyphicon-align-center',
		'text' : '',
		'exec' : function() {
			document.execCommand(this.name, false, null);
		}
	}, {
		'name' : 'justifyFull',
		'icon' : 'glyphicon glyphicon-align-justify',
		'text' : '',
		'exec' : function() {
			document.execCommand(this.name, false, null);
		}
	}, {
		'name' : 'justifyLeft',
		'icon' : 'glyphicon glyphicon-align-left',
		'text' : '',
		'exec' : function() {
			document.execCommand(this.name, false, null);
		}
	}, {
		'name' : 'justifyRight',
		'icon' : 'glyphicon glyphicon-align-right',
		'text' : '',
		'exec' : function() {
			document.execCommand(this.name, false, null);
		}
	}, {
		'name' : 'removeFormat',
		'icon' : 'glyphicon glyphicon-ban-circle',
		'text' : '',
		'exec' : function() {
			document.execCommand(this.name, false, null);
		}
	}, {
		'name' : 'strikeThrough',
		'icon' : '',
		'text' : '<strike>S</strike>',
		'exec' : function() {
			document.execCommand(this.name, false, null);
		}
	}, {
		'name' : 'subscript',
		'icon' : '',
		'text' : 'S<sub>ub</sub>',
		'exec' : function() {
			document.execCommand(this.name, false, null);
		}
	}, {
		'name' : 'superscript',
		'icon' : '',
		'text' : 'S<sup>up</sup>',
		'exec' : function() {
			document.execCommand(this.name, false, null);
		}
	}, {
		'name' : 'underline',
		'icon' : '',
		'text' : '<u>U</u>',
		'exec' : function() {
			document.execCommand(this.name, false, null);
		}
	}, {
		'name' : 'custom',
		'icon' : '',
		'text' : 'custom',
		'exec' : function() {
			var html = prompt('Custom HTML', '');
			document.execCommand('insertHTML', false, html);
		}
	} ]);
});