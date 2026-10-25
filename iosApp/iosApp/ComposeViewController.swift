import UIKit
import shared

class ComposeViewController: UIViewController {
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = UIColor.systemBackground
        
        let composeView = IOSAppContentBridge()
        composeView.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(composeView)
        
        NSLayoutConstraint.activate([
            composeView.topAnchor.constraint(equalTo: view.topAnchor),
            composeView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            composeView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            composeView.bottomAnchor.constraint(equalTo: view.bottomAnchor)
        ])
    }
}
